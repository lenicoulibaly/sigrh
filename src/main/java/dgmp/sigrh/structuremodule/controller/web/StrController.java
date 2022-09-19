package dgmp.sigrh.structuremodule.controller.web;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.str.CreateStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.structuremodule.model.dtos.str.StrTreeView;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller @RequiredArgsConstructor
public class StrController
{
    private final IStrService strService;
    private final StrRepo strRepo;
    private final StrMapper strMapper;

    @GetMapping(path = "/sigrh/structures")
    public String gotoStrLayout(Model model)
    {
        return "structures/str-layout";
    }

    @GetMapping(path = "/sigrh/structures/str-details/{strId}")
    public String gotoStrDetails(Model model, @PathVariable Long strId)
    {
        model.addAttribute("viewMode", "details");
        model.addAttribute("str", strRepo.findById(strId).orElse(null));
        List<Structure> parents = strService.getParents(strId);
        model.addAttribute("parents", parents);
        return "structures/strDetails";
    }

    @GetMapping(path = "/sigrh/structures/new-str-form")
    public String gotoStrForm(Model model)
    {
        model.addAttribute("str", new CreateStrDTO());
        model.addAttribute("viewMode", "new");
        return "structures/newStrForm";
    }

    @PostMapping(path = "/sigrh/structures/str/create")
    public String createStr(RedirectAttributes ra, Model model, @Valid CreateStrDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return gotoStrForm(model);
        }
        strService.createStr(dto);
        return "redirect:/sigrh/structures/new-str-form";
    }

    @GetMapping(path = "/sigrh/structures/child-type/{childTypeId}") @ResponseBody
    public List<ReadStrDTO> getStrByChildType(@PathVariable Long childTypeId)
    {
        List<Structure> strs = strRepo.findByChildType(childTypeId);
        return strs.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
    }

    @GetMapping(path = "/sigrh/structures/loadStrTreeView/{strId}") @ResponseBody
    public List<StrTreeView> loadStrTreeView(@PathVariable Long strId)
    {
        return this.strService.loadStrTreeView(strId);
    }

    @GetMapping(path = "/sigrh/structures/countAllChildren/{strId}") @ResponseBody
    public long countAllChildren(@PathVariable Long strId)
    {
        return this.strService.countAllChildren(strId);
    }
}
