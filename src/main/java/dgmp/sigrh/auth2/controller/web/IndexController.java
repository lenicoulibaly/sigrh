package dgmp.sigrh.auth2.controller.web;

import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class IndexController
{
    private final ISecurityContextManager scm;
    @GetMapping(path = {"/", "/sigrh"})
    //@PreAuthorize("hasRole('USER')")
    public String helloWord(RedirectAttributes ra)
    {
        ra.addAttribute("userId", scm.getAuthUserId());
        return "redirect:/sigrh/assignations/user-ass-list?userId=" + scm.getAuthUserId();
    }

    @GetMapping(path = "/hello-world2")
    @PreAuthorize("hasRole('USER2')")
    public String helloWord2()
    {
        return "hello-world";
    }
}