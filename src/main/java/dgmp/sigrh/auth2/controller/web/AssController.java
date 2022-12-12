package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.controller.repositories.*;
import dgmp.sigrh.auth2.controller.services.spec.IAssService;
import dgmp.sigrh.auth2.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth2.model.dtos.appprivilege.SelectedPrvDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.ReadUserDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.UserMapper;
import dgmp.sigrh.auth2.model.dtos.asignation.*;
import dgmp.sigrh.auth2.model.entities.*;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.str.IStrService;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller @RequiredArgsConstructor
public class AssController
{
    private final IAssService assService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final IStrService strService;
    private final ISecurityContextManager scm;
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final RoleRepo roleRepo;
    private final IPrivilegeService prvService;
    private final PrincipalAssRepo paRepo;
    private final AssignationMapper assMapper;
    private final RoleAssRepo roleAssRepo;
    private final PrvAssRepo prvAssRepo;

    @GetMapping(path = "/sigrh/assignations/new-principal-ass-form")
    public String gotoNewPrincipalAssForm(Model model, @RequestParam(required = false) Long userId)
    {
        Long visibilityId = scm.getVisibilityId();
        List<Structure>  structures= visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId);
        List< ReadStrDTO > strDtos = structures.stream().map(strMapper::mapToReadStrDTO).peek(str->str.setHierarchySigles(strService.getHierarchySigles(str.getStrId()))).collect(Collectors.toList());

        List<AppUser>  users= visibilityId == null ? userRepo.findAll() : userRepo.findAllUsersUnderStr(visibilityId);
        List< ReadUserDTO > userDtos = users.stream().map(userMapper::mapToReadUserDTO).collect(Collectors.toList());
        CreatePrincipalAssDTO dto = new CreatePrincipalAssDTO(); dto.setUserId(userId);
        model.addAttribute("users", userDtos);
        model.addAttribute("structures", strDtos);
        model.addAttribute("dto", dto);

        if(userId == null) {
            model.addAttribute("user", new ReadUserDTO());
            return "security/assignations/newPrincipalAssForm";
        }
        AppUser appUser = userRepo.findById(userId).orElse(null);
        if(appUser == null) {
            model.addAttribute("user", new ReadUserDTO());
            return "security/assignations/newPrincipalAssForm";
        }
        ReadUserDTO user = userMapper.mapToReadUserDTO(appUser);
        model.addAttribute("user", user);
        return "security/assignations/newPrincipalAssForm";
    }

    @PostMapping(path = "/sigrh/assignations/create-principal-ass")
    public String createPrincipalAss(Model model, RedirectAttributes ra, @Valid CreatePrincipalAssDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoNewPrincipalAssForm(model, dto.getUserId());
        }
        assService.createPrincipalAss(dto);
        ra.addAttribute("userId", dto.getUserId());
        return "redirect:/sigrh/assignations/new-principal-ass-form";
    }

    @GetMapping(path = "/sigrh/assignations/update-principal-ass-form")
    public String gotoUpdatePrincipalAssForm(Model model, @RequestParam Long assId)
    {
        Long visibilityId = scm.getVisibilityId();
        List<Structure>  structures= visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId);
        List< ReadStrDTO > strDtos = structures.stream().map(strMapper::mapToReadStrDTO).peek(str->str.setHierarchySigles(strService.getHierarchySigles(str.getStrId()))).collect(Collectors.toList());

        List<AppUser>  users= visibilityId == null ? new ArrayList<>() : userRepo.findAllUsersUnderStr(visibilityId);
        PrincipalAss principalAss  = paRepo.findById(assId).orElse(null);
        if(principalAss == null)
        {
            model.addAttribute("globalErrorMsg", "Assignation inexistante");
            return "security/assignations/updatePrincipalAssForm";
        }
        Long userId = paRepo.getUserId(assId);
        UpdatePrincipalAssDTO dto = assMapper.mapToUpdatePrincipalAssDTO(principalAss);
        principalAss.getStrDTO().setHierarchySigles(strRepo.getHierarchySigle(principalAss.getStructure().getStrId()));
        model.addAttribute("prAss", principalAss);
        model.addAttribute("structures", strDtos);
        model.addAttribute("dto", dto);

        if(userId == null) {
            model.addAttribute("user", new ReadUserDTO());
            return "security/assignations/updatePrincipalAssForm";
        }
        AppUser appUser = userRepo.findById(userId).orElse(null);
        if(appUser == null) {
            model.addAttribute("user", new ReadUserDTO());
            return "security/assignations/updatePrincipalAssForm";
        }
        ReadUserDTO user = userMapper.mapToReadUserDTO(appUser);
        model.addAttribute("user", user);
        return "security/assignations/updatePrincipalAssForm";
    }

    @PostMapping(path = "/sigrh/assignations/update-principal-ass")
    public String updatePrincipalAss(Model model, RedirectAttributes ra, @Valid UpdatePrincipalAssDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage().contains("::"))
                {
                    String[] errTab = ge.getDefaultMessage().split("::");
                    model.addAttribute(errTab[0] + "ErrMsg", errTab[1]);
                }
                else  model.addAttribute("globalErrMsg", ge.getDefaultMessage());
            });
            return this.gotoUpdatePrincipalAssForm(model, dto.getAssId());
        }
        assService.updatePrincipalAss(dto);
        ra.addAttribute("assId", dto.getAssId());
        return "redirect:/sigrh/assignations/auth-list";
    }

    @GetMapping(path = "/sigrh/assignations/prv-to-role-ass-form")
    public String gotoPrvToRoleAssForm(Model model, @RequestParam(required = false) Long roleId)
    {
        PrvsToRoleDTO dto = new PrvsToRoleDTO(); dto.setRoleId(roleId);
        model.addAttribute("dto", dto);

        if(roleId == null) {
            model.addAttribute("role", new AppRole());
            return "security/assignations/prvToRoleAssForm";
        }
        AppRole role = roleRepo.findById(roleId).orElse(null);
        if(role == null) {
            model.addAttribute("user", new AppRole());
            return "security/assignations/prvToRoleAssForm";
        }
        List<SelectedPrvDTO> prvs = prvService.getSelectedPrvs(Collections.singleton(roleId));
        model.addAttribute("role", role);
        model.addAttribute("prvs", prvs);
        return "security/assignations/prvToRoleAssForm";
    }

    @PostMapping(path = "/sigrh/assignations/add-prvs-to-role")
    public String addPrvsToRole(Model model, RedirectAttributes ra, @Valid PrvsToRoleDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoPrvToRoleAssForm(model, dto.getRoleId());
        }
        assService.setRolePrivileges(dto);
        ra.addAttribute("roleId", dto.getRoleId());
        return "redirect:/sigrh/assignations/prv-to-role-ass-form";
    }

    @GetMapping(path = "/sigrh/assignations/user-ass-list")
    public String gotoAssList(Model model, @RequestParam Long userId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Page<ReadPrincipalAssDTO> assList = paRepo.searchPrincipalAssByUser(userId, StringUtils.stripAccentsToUpperCase(key), PageRequest.of(pageNum, pageSize));
        model.addAttribute("username", userRepo.getUsername(userId));
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("assList", assList);
        model.addAttribute("pages", new long[assList.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        return "security/assignations/assList";
    }

    @GetMapping(path = "/sigrh/assignations/set-as-default")
    public String setAssAsDefault(RedirectAttributes ra, @RequestParam Long assId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        assService.setPrincipalAssAsDefault(assId);
        /*ra.addAttribute("pageNum", pageNum);
        ra.addAttribute("currentPage", pageNum);
        ra.addAttribute("pageSize", pageSize);
        ra.addAttribute("key", key);
        ra.addAttribute("userId", paRepo.getUserId(assId));*/
        return "redirect:/sigrh/index";
    }

    @GetMapping(path = "/sigrh/assignations/restore-ass")
    public String restorePrincipalAss(RedirectAttributes ra, @RequestParam Long assId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        assService.restorePrincipalAss(assId);
        ra.addAttribute("pageNum", pageNum);
        ra.addAttribute("currentPage", pageNum);
        ra.addAttribute("pageSize", pageSize);
        ra.addAttribute("key", key);
        ra.addAttribute("userId", paRepo.getUserId(assId));
        return "redirect:/sigrh/assignations/user-ass-list";
    }

    @GetMapping(path = "/sigrh/assignations/revoke-ass")
    public String revokePrincipalAss(RedirectAttributes ra, @RequestParam Long assId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        assService.revokePrincipalAss(assId);
        ra.addAttribute("pageNum", pageNum);
        ra.addAttribute("currentPage", pageNum);
        ra.addAttribute("pageSize", pageSize);
        ra.addAttribute("key", key);
        ra.addAttribute("userId", paRepo.getUserId(assId));
        return "redirect:/sigrh/assignations/user-ass-list";
    }

    @GetMapping(path = "/sigrh/assignations/auth-list")
    public String gotoAuthList(Model model, @RequestParam Long assId, @RequestParam(defaultValue = "0") int rolePageNum, @RequestParam(defaultValue = "5") int rolePageSize, @RequestParam(defaultValue = "") String roleKey,
                               @RequestParam(defaultValue = "0") int prvPageNum, @RequestParam(defaultValue = "5") int prvPageSize, @RequestParam(defaultValue = "") String prvKey)
    {
        PrincipalAss prAsss = paRepo.findById(assId).orElse(null);
        if(prAsss == null) return "security/assignations/authList.html";
        prAsss.getStrDTO().setHierarchySigles(strRepo.getHierarchySigle(prAsss.getStructure().getStrId()));

        Page<RoleAss> roleAssPage = roleAssRepo.searchActiveByPrincipalAss(assId, StringUtils.stripAccentsToUpperCase(roleKey), PageRequest.of(rolePageNum, rolePageSize));
        Page<PrvAss> prvPage = prvAssRepo.searchActivePrvsByPrincipalAss(assId, StringUtils.stripAccentsToUpperCase(prvKey), PageRequest.of(prvPageNum, prvPageSize));

        model.addAttribute("prAsss", prAsss);
        model.addAttribute("user", prAsss.getUser());
        model.addAttribute("assId", assId);

        model.addAttribute("rolePageNum", rolePageNum);
        model.addAttribute("roleCurrentPage", rolePageNum);
        model.addAttribute("rolePageSize", rolePageSize);
        model.addAttribute("roleKey", roleKey);
        model.addAttribute("roleAssPage", roleAssPage);
        model.addAttribute("rolePages", new long[roleAssPage.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        model.addAttribute("prvPageNum", prvPageNum);
        model.addAttribute("prvCurrentPage", prvPageNum);
        model.addAttribute("prvPageSize", prvPageSize);
        model.addAttribute("prvKey", prvKey);
        model.addAttribute("prvPage", prvPage);
        model.addAttribute("prvPages", new long[prvPage.getTotalPages()]);

        return "security/assignations/authList.html";
    }

    @PostMapping(path = "/sigrh/assignations/remove-role-to-principal-ass")
    public String removeRoleToPrincipalAss(RedirectAttributes ra, @RequestParam Long assId, @RequestParam Long roleId)
    {
        RolesAssDTO dto = new RolesAssDTO();
        dto.setPrincipalAssId(assId);
        dto.setRoleIds(new HashSet<>(Collections.singletonList(roleId)));
        assService.removeRolesToPrincipalAss(dto);
        ra.addAttribute("assId", assId);
        return "redirect:/sigrh/assignations/auth-list";
    }

    @PostMapping(path = "/sigrh/assignations/remove-prv-to-principal-ass")
    public String removePrvToPrincipalAss(RedirectAttributes ra, @RequestParam Long assId, @RequestParam Long prvId)
    {
        PrvsAssDTO dto = new PrvsAssDTO();
        dto.setPrincipalAssId(assId);
        dto.setPrvIds(new HashSet<>(Collections.singletonList(prvId)));
        assService.removePrivilegesToPrincipalAss(dto);
        ra.addAttribute("assId", assId);
        return "redirect:/sigrh/assignations/auth-list";
    }
}