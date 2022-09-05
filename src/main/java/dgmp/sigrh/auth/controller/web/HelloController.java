package dgmp.sigrh.auth.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController
{
    @GetMapping(path = "/hello-world")
    @PreAuthorize("hasRole('USER')")
    public String helloWord()
    {
        return "hello-world";
    }

    @GetMapping(path = "/hello-world2")
    @PreAuthorize("hasRole('USER2')")
    public String helloWord2()
    {
        return "hello-world";
    }
}
