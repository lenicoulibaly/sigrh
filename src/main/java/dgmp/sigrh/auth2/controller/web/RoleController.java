package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.controller.services.spec.IRoleService;
import dgmp.sigrh.auth2.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth2.model.dtos.approle.ReadRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RoleController
{
    private final IRoleService roleService;

    @GetMapping(path = "/sigrh/security/roles/new-role-form")
    public String gotoNewRoleForm(Model model)
    {
        CreateRoleDTO dto = new CreateRoleDTO();
        model.addAttribute("dto", dto);
        return "security/roles/newRoleForm";
    }

    @PostMapping(path = "/sigrh/security/roles/create")
    public String createRole(Model model, RedirectAttributes ra, @Valid CreateRoleDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoNewRoleForm(model);
        }
        roleService.createRole(dto);
        return "redirect:/sigrh/security/roles/new-role-form";
    }

    @GetMapping(path = "/sigrh/roles/role-list")
    public String gotoPrvsList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Page<ReadRoleDTO> roles = roleService.searchRoles(key, PageRequest.of(pageNum, pageSize));

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("roles", roles);
        model.addAttribute("pages", new long[roles.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        return "security/roles/roleList";
    }
}
