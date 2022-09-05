package dgmp.sigrh.agentmodule.controller.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/staffadmin/agents")
public class AgentResources
{
    @GetMapping(path = "/test")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String test()
    {
        return "test success";
    }
}
