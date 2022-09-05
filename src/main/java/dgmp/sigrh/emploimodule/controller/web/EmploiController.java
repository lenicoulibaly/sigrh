package dgmp.sigrh.emploimodule.controller.web;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import dgmp.sigrh.emploimodule.controller.service.IEmploiService;
import dgmp.sigrh.emploimodule.model.dtos.CreateEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.EmploiMapper;
import dgmp.sigrh.emploimodule.model.dtos.ReadEmploiDTO;
import dgmp.sigrh.emploimodule.model.dtos.UpdateEmploiDTO;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class EmploiController
{
    private final IEmploiService emploiService;
    private final EmploiDAO emploiDAO;
    private final EmploiMapper emploiMapper;
    private final IHistoService<Emploi, EmploiHisto, EmploiEventType> emploiHistoService;

    @PostMapping(path = "/sigrh/administration/emplois/create")
    public String createEmploi(Model model, @Valid CreateEmploiDTO dto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            model.addAttribute("emploi", dto);
            model.addAttribute("viewMode", "new");
            return "administration/emplois/newEmploiForm";
        }
        emploiService.createEmploi(dto);
        return "redirect:/sigrh/administration/emplois/emplois-list";
    }

    @PostMapping(path = "/sigrh/administration/emplois/update")
    public String updateEmploi(Model model, @Valid UpdateEmploiDTO dto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            bindingResult.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage().contains("::"))
                {
                    String field= ge.getDefaultMessage().split("::")[0];
                    String message= ge.getDefaultMessage().split("::")[1];
                    model.addAttribute(field +"ErrMsg", message);
                }
            });
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            model.addAttribute("viewMode", "update");
            //dto.setNomEmploi(emploiDAO.getNomEmploi(dto.getIdEmploi()));
            model.addAttribute("emploi", dto);
            return "administration/emplois/updateEmploiForm";
        }
        emploiService.updateEmploi(dto);
        return "redirect:/sigrh/administration/emplois/emplois-list";
    }

    @GetMapping(path = "/sigrh/administration/emplois/delete")
    public String deleteEmploi(@RequestParam(defaultValue = "0") Long idEmploi)
    {
        emploiService.deleteEmploi(idEmploi);
        return "redirect:/sigrh/administration/emplois/emplois-list";
    }

    @GetMapping(path = "/sigrh/administration/emplois/restore-emploi")
    public String restoreEmploi(@RequestParam(defaultValue = "0") Long idEmploi)
    {
        emploiService.restoreEmploi(idEmploi);
        return "redirect:/sigrh/administration/emplois/deleted-emplois-list";
    }

    @GetMapping(path = "/sigrh/administration/emplois/emplois-list")
    public String gotoEmploisList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        Page<ReadEmploiDTO> emplois = emploiService.searchPageOfEmplois(key, pageNum, pageSize);
        model.addAttribute("emplois", emplois);
        model.addAttribute("pages", new long[emplois.getTotalPages()]);
        model.addAttribute("viewMode", "list");
        return "administration/emplois/emploisList";
    }

    @GetMapping(path = "/sigrh/administration/emplois/emploi-details")
    public String gotoEmploiDetails(Model model, @RequestParam(defaultValue = "0") long idEmploi,
                                    @RequestParam(defaultValue = "0") int pageNum,
                                    @RequestParam(defaultValue = "20") int pageSize,
                                    @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusYears(2)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime after,
                                    @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime befor)
    {
        Emploi emploi =emploiDAO.findById(idEmploi).orElse(null);
        model.addAttribute("emploi", emploi == null ? new ReadEmploiDTO() : emploiMapper.mapToReadEmploiDTO(emploi));
        model.addAttribute("modificationList", emploiHistoService.getHistoPageBetweenPeriod(idEmploi, after, befor, pageNum, pageSize));
        model.addAttribute("viewMode", "details");
        return "administration/emplois/emploiDetails";
    }

    @GetMapping(path = "/sigrh/administration/emplois/new-emploi-form")
    public String gotoNewEmploiForm(Model model)
    {
        model.addAttribute("emploi", new CreateEmploiDTO());
        model.addAttribute("viewMode", "new");
        return "administration/emplois/newEmploiForm";
    }

    @GetMapping(path = "/sigrh/administration/emplois/update-emploi-form")
    public String gotoUpdateEmploiForm(Model model, @RequestParam(defaultValue = "0") long idEmploi)
    {
        Emploi loadedEmploi = emploiDAO.findById(idEmploi).orElse(null);
        UpdateEmploiDTO  emploiDTO = loadedEmploi == null ? new UpdateEmploiDTO() : new UpdateEmploiDTO(loadedEmploi.getIdEmploi(), loadedEmploi.getNomEmploi());
        model.addAttribute("emploi", emploiDTO);
        model.addAttribute("viewMode", "update");
        return "administration/emplois/updateEmploiForm";
    }

    @GetMapping(path = "/sigrh/administration/emplois/deleted-emplois-list")
    public String gotoDeletedEmploisList(Model model, @RequestParam(defaultValue = "")String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        Page<ReadEmploiDTO> emplois = emploiService.searchPageOfDeletedEmplois(key, pageNum, pageSize);
        model.addAttribute("emplois", emplois);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("emplois", emplois);
        model.addAttribute("pages", new long[emplois.getTotalPages()]);
        model.addAttribute("viewMode", "trash");
        return "administration/emplois/deletedEmploisList";
    }
}
