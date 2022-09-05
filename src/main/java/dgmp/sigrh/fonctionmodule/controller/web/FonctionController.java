package dgmp.sigrh.fonctionmodule.controller.web;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionDAO;
import dgmp.sigrh.fonctionmodule.controller.service.IFonctionService;
import dgmp.sigrh.fonctionmodule.model.dtos.CreateFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.ReadFonctionDTO;
import dgmp.sigrh.fonctionmodule.model.dtos.UpdateFonctionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FonctionController
{
    private final IFonctionService fonctionService;
    private final FonctionDAO fonctionDAO;

    @PostMapping(path = "/sigrh/administration/fonctions/create")
    public String createFonction(Model model, RedirectAttributes ra, @Valid CreateFonctionDTO dto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            model.addAttribute("fonction", dto);
            model.addAttribute("viewMode", "new");
            return "administration/fonctions/newFonctionForm";
        }
        fonctionService.createFonction(dto);
        return "redirect:/sigrh/administration/fonctions/fonctions-list";
    }

    @PostMapping(path = "/sigrh/administration/fonctions/update")
    public String updateFonction(RedirectAttributes ra, Model model, @Valid UpdateFonctionDTO dto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            List<String> globalErrMsg = new ArrayList<>();
            bindingResult.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage().contains("::"))
                {
                    String field= ge.getDefaultMessage().split("::")[0];
                    String message= ge.getDefaultMessage().split("::")[1];
                    model.addAttribute(field +"ErrMsg", message);
                }else
                {
                    globalErrMsg.add(ge.getDefaultMessage());
                }

            });
            model.addAttribute("globalErrMsg", globalErrMsg);
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            model.addAttribute("viewMode", "update");
            model.addAttribute("fonction", dto);
            return "administration/fonctions/updateFonctionForm";
        }
        fonctionService.updateFonction(dto);
        return "redirect:/sigrh/administration/fonctions/fonctions-list";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/delete-fonction")
    public String deleteFonction(Model model, @RequestParam(defaultValue = "0") long idFonction)
    {
        fonctionService.deleteFonction(idFonction);
        return "redirect:/sigrh/administration/fonctions/fonctions-list";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/restore-fonction")
    public String restoreFonction(Model model, @RequestParam(defaultValue = "0") Long idFonction)
    {
        fonctionService.restoreFonction(idFonction);
        return "redirect:/sigrh/administration/fonctions/deleted-fonctions-list";
    }



    @GetMapping(path = "/sigrh/administration/fonctions/fonctions-list")
    public String gotoFonctionsList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        Page<ReadFonctionDTO> fonctions = fonctionService.searchPageOfFonctions(key, pageNum, pageSize);
        model.addAttribute("fonctions", fonctions);
        model.addAttribute("pages", new long[fonctions.getTotalPages()]);
        model.addAttribute("viewMode", "list");
        return "administration/fonctions/fonctionsList";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/fonction-details")
    public String gotoFonctionDetails(Model model, @RequestParam(defaultValue = "0") long idFonction)
    {
        model.addAttribute("fonction", fonctionDAO.findById(idFonction).orElse(null));
        model.addAttribute("modificationList", fonctionService.getModificationHisto(idFonction));
        model.addAttribute("viewMode", "details");
        return "administration/fonctions/fonctionDetails";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/new-fonction-form")
    public String gotoNewFonctionForm(Model model)
    {
        model.addAttribute("fonction", new CreateFonctionDTO());
        model.addAttribute("viewMode", "new");
        return "administration/fonctions/newFonctionForm";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/update-fonction-form")
    public String gotoUpdateFonctionForm(Model model, @RequestParam(defaultValue = "0") long idFonction)
    {
        model.addAttribute("fonction", fonctionDAO.existsById(idFonction) ? fonctionDAO.findById(idFonction).get(): new UpdateFonctionDTO());
        model.addAttribute("viewMode", "update");
        return "administration/fonctions/updateFonctionForm";
    }

    @GetMapping(path = "/sigrh/administration/fonctions/deleted-fonctions-list")
    public String gotoDeletedFonctionsList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Page<ReadFonctionDTO> fonctions = fonctionService.searchPageOfDeletedFonctions(key, pageNum, pageSize);
        model.addAttribute("fonctions", fonctions);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("fonctions", fonctions);
        model.addAttribute("pages", new long[fonctions.getTotalPages()]);
        model.addAttribute("viewMode", "trash");
        return "administration/fonctions/deletedFonctionsList";
    }
}
