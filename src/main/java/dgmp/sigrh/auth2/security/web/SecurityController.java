package dgmp.sigrh.auth2.security.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Controller
public class SecurityController
{
    @GetMapping(path = "/login")
    public String gotoLoginPage(Model model, @RequestParam(required = false) String errMsg)
    {
        if(errMsg != null) model.addAttribute("errMsg", new String(Base64.getUrlDecoder().decode(errMsg)));
        return "security/login";
    }

    @GetMapping(path = "/logout")
    public String Logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "security/login";
    }

    @GetMapping(path = "/sigrh/index") @PreAuthorize("isFullyAuthenticated()")
    public String index(Model model) {
        return "index";
    }
}