package dgmp.sigrh.instancemodule.controller.web;

import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.instancemodule.controller.repositories.InstanceRepo;
import dgmp.sigrh.instancemodule.controller.services.IInstanceService;
import dgmp.sigrh.instancemodule.model.dtos.CreateInstanceDTO;
import dgmp.sigrh.instancemodule.model.dtos.ReadInstanceDTO;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;

@Controller @RequiredArgsConstructor
public class InstanceController
{
    private final InstanceRepo instanceRepo;
    private final StrRepo strRepo;
    private final ISecurityContextManager scm;
    private final StrMapper strMapper;
    private final IInstanceService instanceService;

    @GetMapping(path = "/sigrh/instances/new-instance-form")
    public String gotoNewInstanceForm(Model model)
    {
        Long visibilityId = scm.getVisibilityId();
        List<ReadStrDTO> structures = visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList());
        model.addAttribute("structures", structures);
        model.addAttribute("dto", new CreateInstanceDTO());
        return "administration/instances/newInstanceForm";
    }

    @PostMapping(path = "/sigrh/instances/create")
    public String createInstance(Model model, RedirectAttributes ra, @Valid CreateInstanceDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoNewInstanceForm(model);
        }
        instanceService.createInstance(dto);
        return "redirect:/sigrh/instances/new-instance-form";
    }

    @GetMapping(path = "/sigrh/instances/instance-list")
    public String gotoNewInstanceForm(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Long visibilityId = scm.getVisibilityId();
        Page<ReadInstanceDTO> instances = visibilityId == null ? new PageImpl<>(new ArrayList<>()) : instanceRepo.searchInstances(visibilityId, StringUtils.stripAccentsToUpperCase(key).trim(), PageRequest.of(pageNum, pageSize));
        model.addAttribute("instances", instances);
        model.addAttribute("dto", new CreateInstanceDTO());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("pages", new long[instances.getTotalPages()]);
        return "administration/instances/instanceList";
    }
}
