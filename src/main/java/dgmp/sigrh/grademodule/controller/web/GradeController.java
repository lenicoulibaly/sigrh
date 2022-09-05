package dgmp.sigrh.grademodule.controller.web;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.grademodule.controller.repositories.GradeDAO;
import dgmp.sigrh.grademodule.controller.service.IGradeService;
import dgmp.sigrh.grademodule.model.dtos.CreateGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.GrageMapper;
import dgmp.sigrh.grademodule.model.dtos.ReadGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.UpdateGradeDTO;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.grademodule.model.entities.GradeHisto;
import dgmp.sigrh.grademodule.model.events.GradeEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller @RequiredArgsConstructor
public class GradeController
{
    private final IGradeService gradeService;
    private final GradeDAO gradeDAO;
    private final GrageMapper gradeMapper;
    private final IHistoService<Grade, GradeHisto, GradeEventType> grageHistoService;

    @GetMapping(path = "/sigrh/administration/grades/grades-list")
    public String gotoGradeList(Model model, @RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        Page<ReadGradeDTO> grades = gradeService.searchPageOfGrades(key, pageNum, pageSize);
        model.addAttribute("grades", grades);
        model.addAttribute("pages", new long[grades.getTotalPages()]);
        model.addAttribute("viewMode", "list");
        return "administration/grades/gradesList";
    }

    @GetMapping(path = "/sigrh/administration/grades/grade-details")
    public String gotoGradeDetails(Model model, @RequestParam(defaultValue = "0") long idGrade,
    @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "20") int pageSize,
    @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().minusYears(2)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime after,
    @RequestParam(defaultValue = "#{T(java.time.LocalDateTime).now().plusDays(1)}") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") LocalDateTime befor)
    {
        Grade grade = gradeDAO.findById(idGrade).orElse(null);
        model.addAttribute("grade", grade == null ? new ReadGradeDTO() : gradeMapper.mapToReadGradeDTO(grade));
        model.addAttribute("modificationList", grageHistoService.getHistoPageBetweenPeriod(idGrade, after, befor, pageNum, pageSize));
        model.addAttribute("viewMode", "details");
        return "administration/grades/gradeDetails";
    }

    @GetMapping(path ="/sigrh/administration/grades/deleted-grades-list")
    public String gotoDeletedGradesList(Model model, @RequestParam(defaultValue = "")String key, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        Page<ReadGradeDTO> grades = gradeService.searchPageOfDeletedGrades(key, pageNum, pageSize);
        model.addAttribute("grades", grades);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("grades", grades);
        model.addAttribute("pages", new long[grades.getTotalPages()]);
        model.addAttribute("viewMode", "trash");
        return "administration/grades/deletedGradesList";
    }

    @GetMapping(path = "/sigrh/administration/grades/new-grade-form")
    public String gotoNewGradesForm(Model model)
    {
        model.addAttribute("grade", new CreateGradeDTO());
        model.addAttribute("viewMode", "new");
        return "administration/grades/newGradeForm";
    }

    @GetMapping(path = "/sigrh/administration/grades/update-grade-form")
    public String gotoUpdateGradeForm(Model model, @RequestParam(defaultValue = "0") long idGrade)
    {
        Grade loadedGrade = gradeDAO.findById(idGrade).orElse(null);
        UpdateGradeDTO gradeDTO = loadedGrade == null ? new UpdateGradeDTO() : new UpdateGradeDTO(loadedGrade.getIdGrade(), loadedGrade.getRang(), loadedGrade.getCategorie().name());
        model.addAttribute("grade", gradeDTO);
        model.addAttribute("viewMode", "update");
        return "administration/grades/updateGradeForm";
    }

    //==================================

    @PostMapping(path = "/sigrh/administration/grades/create")
    public String createGrade(Model model, @Valid CreateGradeDTO dto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            bindingResult.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            model.addAttribute("grade", dto);
            model.addAttribute("viewMode", "new");
            return "administration/grades/newGradeForm";
        }
        gradeService.createGrade(dto);
        return "redirect:/sigrh/administration/grades/grades-list";
    }

    @PostMapping(path = "/sigrh/administration/grades/update")
    public String updateGrade(Model model, @Valid UpdateGradeDTO dto, BindingResult bindingResult)
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
                else
                {
                    model.addAttribute("globalErrMsg", ge.getDefaultMessage());
                }
            });
            bindingResult.getFieldErrors().forEach(fe->model.addAttribute(fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            model.addAttribute("viewMode", "update");
            model.addAttribute("grade", dto);
            return "administration/grades/updateGradeForm";
        }
        gradeService.updateGrade(dto);
        return "redirect:/sigrh/administration/grades/grades-list";
    }

    @GetMapping(path = "/sigrh/administration/grades/delete")
    public String deleteGrade(Model model, @RequestParam(defaultValue = "0") Long idGrade)
    {
        gradeService.deleteGrade(idGrade);
        return "redirect:/sigrh/administration/grades/grades-list";
    }

    @GetMapping(path = "/sigrh/administration/grades/restore")
    public String restoreGrade(Model model, @RequestParam(defaultValue = "0") Long idGrade)
    {
        gradeService.restoreGrade(idGrade);
        return "redirect:/sigrh/administration/grades/deleted-grades-list";
    }
}
