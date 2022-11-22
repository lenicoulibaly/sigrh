package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.controller.repositories.AccountTokenRepo;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.controller.services.spec.IUserService;
import dgmp.sigrh.auth2.model.dtos.appuser.ActivateAccountDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.CreateUserDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.ReadUserDTO;
import dgmp.sigrh.auth2.model.entities.AccountToken;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.controller.service.str.StrService;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController
{
    private final UserRepo userRepo;
    private final IUserService userService;
    private final AccountTokenRepo tokenRepo;
    private final ISecurityContextManager scm;
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final StrService strService;


    @GetMapping(path = "/sigrh/logout")
    public String logout(Model model, HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/login";
    }

    @GetMapping(path = "/sigrh/security/users/new-user-form")
    public String gotoCreateUserForm(Model model)
    {
        Long visibilityId = scm.getVisibilityId();
        List<Structure> structures= visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId);
        List<ReadStrDTO> strDtos = structures.stream().map(strMapper::mapToReadStrDTO).peek(str->str.setHierarchySigles(strService.getHierarchySigles(str.getStrId()))).collect(Collectors.toList());

        CreateUserDTO dto = new CreateUserDTO();
        model.addAttribute("dto", dto);
        model.addAttribute("structures", strDtos);
        return "security/users/newUserForm";
    }

    @PostMapping(path = "/sigrh/security/users/create")
    public String createUser(Model model, RedirectAttributes ra, @Valid CreateUserDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge->model.addAttribute("globalErrMsg", ge.getDefaultMessage()));
            return this.gotoCreateUserForm(model);
        }
        userService.createUser(dto);
        return "redirect:/sigrh/security/users/new-user-form";
    }

    @GetMapping(path = "/open/sigrh/security/activate-account")  @PreAuthorize("isAnonymous()")
    public String gotoAccountActivationForm(Model model, @RequestParam String token)
    {
        userService.clickLink(token);
        AccountToken accountToken = tokenRepo.findByToken(token).orElse(null);

        if(token == null)
        {
            model.addAttribute("globalErrorMsg", "Lien invalide");
            return "security/users/activateAccountForm";
        }
        ActivateAccountDTO dto = ActivateAccountDTO.builder().userId(accountToken.getUser().getUserId()).activationToken(token).username(accountToken.getUser().getUsername()).build();
        model.addAttribute("dto", dto);
        return "security/users/activateAccountForm";
    }

    @GetMapping(path = "/open/sigrh/security/users/activate-account") @PreAuthorize("isAnonymous()")
    public String activateAccount(Model model, RedirectAttributes ra,
                                  @ModelAttribute @Valid ActivateAccountDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(err->model.addAttribute(err.getField() + "ErrMsg", err.getDefaultMessage()));
            br.getGlobalErrors().forEach(ge-> {
                if(ge.getDefaultMessage().contains("::"))
                {
                    String[] errTab = ge.getDefaultMessage().split("::");
                    model.addAttribute(errTab[0] + "ErrMsg", errTab[1]);
                }
                else
                {
                    model.addAttribute("globalErrMsg", ge.getDefaultMessage());
                }
            });
            return this.gotoAccountActivationForm(model, dto.getActivationToken());
        }
        userService.activateAccount(dto);
        return "redirect:/login";
    }

    @GetMapping(path = "/sigrh/users/user-list")
    public String gotoUserList(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        Long visibilityId = scm.getVisibilityId();
        Page<ReadUserDTO> users = userService.searchUsers(key,visibilityId, PageRequest.of(pageNum, pageSize));
        users.forEach(u->u.setStatus(userService.getUserStatus(u.getUserId())));

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("users", users);
        model.addAttribute("pages", new long[users.getTotalPages()]);
        model.addAttribute("viewMode", "list");

        return "security/users/userList";
    }


    @GetMapping(path = "/sigrh/users/block-user")
    public String blockAccount(Model model, @RequestParam Long userId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        userService.blockAccount(userId);
        return this.gotoUserList(model, pageNum, pageSize, key);
    }

    @GetMapping(path = "/sigrh/users/unblock-user")
    public String unBlockAccount(Model model, @RequestParam Long userId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key)
    {
        userService.unBlockAccount(userId);
        return this.gotoUserList(model, pageNum, pageSize, key);
    }

    @GetMapping(path = "/sigrh/users/send-activation-link")
    public String sendAccountActivationLink(Model model, @RequestParam Long userId, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key) throws IllegalAccessException {
        userService.sendAccountActivationEmail(userId);
        return this.gotoUserList(model, pageNum, pageSize, key);
    }
}
