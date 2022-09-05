package dgmp.sigrh.shared.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdministrationController
{
    @GetMapping(path = "/sigrh/administration")
    public String gotoAdministration(Model model)
    {
        return "administration/administration-layout";
    }
}
