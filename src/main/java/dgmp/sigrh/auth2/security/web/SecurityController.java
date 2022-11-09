package dgmp.sigrh.auth2.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class SecurityController
{
    @GetMapping(path = "/login")
    public String gotoLoginPage()
    {
        return "security/login";
    }

    @GetMapping(path = "/logout")
    public String Logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "security/login";
    }
}