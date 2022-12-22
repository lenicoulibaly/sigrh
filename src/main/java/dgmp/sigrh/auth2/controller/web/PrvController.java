package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth2.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.ReadPrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.SelectedPrvDTO;
import dgmp.sigrh.auth2.model.enums.PrvGroup;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.dtos.ReadTypeDTO;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class PrvController
{
    private final IPrivilegeService prvService;
    private final TypeRepo typeRepo;

    @GetMapping(path = "/sigrh/security/privileges/new-prv-form")
    public String gotoNewPrvForm(Model model)
    {
        CreatePrivilegeDTO dto = new CreatePrivilegeDTO();
        List<ReadTypeDTO> prvTypes = typeRepo.findByTypeGroup(TypeGroup.PRIVILEGE);
        model.addAttribute("prvTypes", prvTypes);
        model.addAttribute("dto", dto);
        model.addAttribute("groups", PrvGroup.getPrvGroups());
        return "security/privileges/newPrvForm";
    }

    @PostMapping(path = "/sigrh/security/privileges/create")
    public String createPrv(Model model, RedirectAttributes ra, @Valid CreatePrivilegeDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoNewPrvForm(model);
        }
        prvService.createPrivilege(dto);
        return "redirect:/sigrh/security/privileges/new-prv-form";
    }

    @GetMapping(path = "/sigrh/privileges/prv-list")
    public String gotoPrvsList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Page<ReadPrivilegeDTO> prvs = prvService.searchPrivileges(StringUtils.stripAccentsToUpperCase(key), PageRequest.of(pageNum, pageSize));

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("prvs", prvs);
        model.addAttribute("pages", new long[prvs.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        return "security/privileges/prvList";
    }

    @ResponseBody
    @GetMapping(path = "/sigrh/privileges/selected-prvs")
    public List<SelectedPrvDTO> getSelectedPrvs(@RequestParam Long prAssId, @RequestParam Set<Long> oldRoleIds, @RequestParam Set<Long> roleIds, @RequestParam Set<Long> prvIds)
    {
        return prvService.getSelectedPrvs(prAssId, oldRoleIds, roleIds, prvIds);
    }


}
