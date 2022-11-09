package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.controller.repositories.AccountTokenRepo;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.controller.services.spec.IUserService;
import dgmp.sigrh.auth2.model.dtos.appuser.ActivateAccountDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.CreateUserDTO;
import dgmp.sigrh.auth2.model.entities.AccountToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UserController
{
    private final UserRepo userRepo;
    private final IUserService userService;
    private final AccountTokenRepo tokenRepo;


    @GetMapping(path = "/sigrh/security/users/new-user-form")
    public String gotoCreateUserForm(Model model)
    {
        CreateUserDTO dto = new CreateUserDTO();
        model.addAttribute("dto", dto);
        return "security/users/newUserForm";
    }

    @PostMapping(path = "/sigrh/security/users/create")
    public String createUser(Model model, RedirectAttributes ra, CreateUserDTO dto, BindingResult br) throws IllegalAccessException
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
        ActivateAccountDTO dto = ActivateAccountDTO.builder().activationToken(token).username(accountToken.getUser().getUsername()).build();
        model.addAttribute("dto", dto);
        return "security/users/activateAccountForm";
    }

    @PostMapping(path = "/open/sigrh/security/users/activate-account") @PreAuthorize("isAnonymous()")
    public String activateAccount(Model model, RedirectAttributes ra, @Valid ActivateAccountDTO dto, BindingResult br)
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
            return this.gotoCreateUserForm(model);
        }
        userService.activateAccount(dto);
        return "redirect:/sigrh/security/users/new-user-form";
    }
}
