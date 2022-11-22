package dgmp.sigrh.agentmodule.controller.web;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.controller.services.IAgentService;
import dgmp.sigrh.agentmodule.model.dtos.CreateAgentDTO;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AgentController
{
    private final IAgentService agentService;
    private final AgentRepo agentRepo;
    private final ISecurityContextManager scm;

    @GetMapping(path = "/")
    public String index(Model model)
    {
        return gotoAgentForm(model);
    }

    @GetMapping(path = "/agents/list")
    public String gotoListAgents(Model model)
    {
        model.addAttribute("agents", agentRepo.findAll());
        return "personnel/personnel";
    }

    @GetMapping(path = "/agents/form")

    public String gotoAgentForm(Model model)
    {
        model.addAttribute("dto", new CreateAgentDTO());
        return "personnel/personnelForm";
    }

    @PostMapping(path = "/agents/create")
    public String createAgent(Model model, @Valid CreateAgentDTO dto, BindingResult br)
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            return gotoAgentForm(model);
        }
        agentService.createAgent(dto);
        return "personnel/personnelConfirmForm";
    }
}
