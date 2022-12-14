package dgmp.sigrh.agentmodule.controller.web;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.controller.services.IAgentService;
import dgmp.sigrh.agentmodule.model.dtos.*;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.enums.Civility;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.agentmodule.model.enums.TypeAgent;
import dgmp.sigrh.archivemodule.controller.service.IArchiveService;
import dgmp.sigrh.archivemodule.controller.service.IFilesManager;
import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiRepo;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionRepo;
import dgmp.sigrh.grademodule.controller.repositories.GradeRepo;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AgentController
{
    private final IAgentService agentService;
    private final AgentRepo agentRepo;
    private final ISecurityContextManager scm;
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final AgentMapper agentMapper;
    private final FonctionRepo fonctionRepo;
    private final EmploiRepo empRepo;
    private final GradeRepo gradeRepo;
    private final IFilesManager filesManager;
    private final IArchiveService archiveService;
    private final TypeRepo typeRepo;

    @GetMapping(path = "/sigrh/agents/index")
    public String index(Model model)
    {
        return "personnel/agent-layout";
    }

    @GetMapping(path = "/sigrh/agents/list")
    public String gotoListAgents(Model model, @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "") String key,
                                 @RequestParam(required = false) List<String> civilities, @RequestParam(required = false) List<String> typeAgents,
                                 @RequestParam(required = false) List<Long> fonctionsIds, @RequestParam(required = false) List<Long> gradesIds,
                                 @RequestParam(required = false) List<Long> emploisIds, @RequestParam(required = false) Long strId)
    {
        Long visibilityId = scm.getVisibilityId();
        strId = strId == null ? visibilityId : strId;
        model.addAttribute("strId", strId );
        model.addAttribute("strList", strRepo.searchReadStrDTO(visibilityId, key, PersistenceStatus.ACTIVE));
        model.addAttribute("civilities", civilities);
        model.addAttribute("typeAgents", typeAgents);
        model.addAttribute("fonctionsIds", fonctionsIds);
        model.addAttribute("gradesIds", gradesIds);
        model.addAttribute("emploisIds", emploisIds);

        Page<ReadAgentDTO> agents = agentService.searchAgentsMultiCriteres(strId,key, Collections.singletonList(EtatRecrutement.EN_SERVICE.name()),
                civilities, typeAgents, fonctionsIds, gradesIds, emploisIds, PageRequest.of(pageNum, pageSize));

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("key", key);
        model.addAttribute("agents", agents);
        model.addAttribute("pages", new long[agents.getTotalPages()]);
        model.addAttribute("viewMode", "list");
        model.addAttribute("typeAgentList", TypeAgent.getAll());
        model.addAttribute("civilityList", Civility.getAll());
        model.addAttribute("fonctionList", fonctionRepo.getActiveFonctions());
        model.addAttribute("emploiList", empRepo.getActiveEmplois());
        model.addAttribute("gradeList", gradeRepo.getActiveGrades());

        return "personnel/personnel";
    }

    @GetMapping(path = "/sigrh/agents/new-agent-form")
    public String gotoAgentForm(Model model)
    {
        Long visibilityId = scm.getVisibilityId();
        List<ReadStrDTO> structures = visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList());
        model.addAttribute("dto", new RegisterAgentDTO());
        model.addAttribute("structures", structures);
        return "personnel/newAgentForm";
    }

    @GetMapping(path = "/sigrh/agents/update-agent-form")
    public String gotoUpdateAgentForm(Model model, @RequestParam Long agtId, UpdateAgentDTO dto)
    {
        //Long visibilityId = scm.getVisibilityId();

        if(dto != null)
        {
            if(dto.getAgentId() != null)
            {
                model.addAttribute("dto", dto);
                return "personnel/updateAgentForm";
            }
        }
        Agent agent = agentRepo.findById(agtId).orElse(null);
        dto = agentMapper.mapToUpdateAgentDTO(agent);
        model.addAttribute("dto", dto);
        return "personnel/updateAgentForm";
    }

    @PostMapping(path = "/sigrh/agents/create")
    public String createAgent(Model model, @Valid CreateNewAgentDTO dto, BindingResult br) throws IllegalAccessException {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            return gotoAgentForm(model);
        }
        agentService.createAgent(dto);
        return "personnel/personnel";
    }

    @PostMapping(path = "/sigrh/agents/update")
    public String updateAgent(Model model, @Valid UpdateAgentDTO dto, BindingResult br) throws IllegalAccessException {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            List<String> globalErrors = new ArrayList<>();
            br.getGlobalErrors().forEach(ge->
            {
                if(ge.getDefaultMessage() != null)
                {
                    if(ge.getDefaultMessage().contains("::"))
                    {
                        String[] errTab = ge.getDefaultMessage().split("::");
                        model.addAttribute(errTab[0] + "ErrMsg", errTab[1]);
                    }
                    else
                    {
                        globalErrors.add(ge.getDefaultMessage());
                    }
                }
            });
            model.addAttribute("globalErrs", globalErrors);
            return gotoUpdateAgentForm(model, dto.getAgentId(), dto);
        }
        agentService.updateAgent(dto);
        return "redirect:/sigrh/agents/list";
    }

    @GetMapping(path = "/sigrh/agents/register/form")
    public String gotoRegisterAgentForm(Model model, RegisterAgentDTO dto)
    {
        Long visibilityId = scm.getVisibilityId();
        List<ReadStrDTO> structures = visibilityId == null ? new ArrayList<>() : strRepo.findAllChildren(visibilityId).stream().map(strMapper::mapToReadSimpleReadStrDto).collect(Collectors.toList());
        model.addAttribute("dto", dto != null ? dto : new RegisterAgentDTO());
        model.addAttribute("structures", structures);
        return "personnel/registerAgentForm";
    }

    @PostMapping(path = "/sigrh/agents/register")
    public String registerAgent(Model model, @Valid RegisterAgentDTO dto, BindingResult br) throws IllegalAccessException
    {
        if(br.hasErrors())
        {
            br.getFieldErrors().forEach(fe -> model.addAttribute( fe.getField() + "ErrMsg", fe.getDefaultMessage()));
            return gotoRegisterAgentForm(model, dto);
        }
        agentService.registerAgent(dto);
        return "redirect:/sigrh/agents/list";
    }

    @GetMapping(path = "/sigrh/agents/profile")
    public String gotoProfilePage(Model model, @RequestParam Long agtId, @RequestParam(defaultValue = "0") int docPageNum,
                                  @RequestParam(defaultValue = "5") int docPageSize, @RequestParam(defaultValue = "") String docKey,
                                  @RequestParam(required = false) List<Long> typeIds, @RequestParam(defaultValue = "infoProf") String tab)
    {
        Agent agent = agentRepo.findById(agtId).orElse(null);
        Page<Archive> archives = archiveService.searchArchives(agtId, docKey, typeIds, PageRequest.of(docPageNum, docPageSize));
        if(agent == null) return "personnel/profile";
        ReadAgentDTO dto = agentMapper.mapToReadAgentDTO(agent);
        model.addAttribute("agent", dto);
        model.addAttribute("archives", archives);


        model.addAttribute("docPageNum", docPageNum);
        model.addAttribute("docCurrentPage", docPageNum);
        model.addAttribute("docPageSize", docPageSize);
        model.addAttribute("docKey", docKey);
        model.addAttribute("tab", tab);
        model.addAttribute("pages", new long[archives.getTotalPages()]);
        model.addAttribute("typeIds", typeIds);
        model.addAttribute("archiveTypeList", typeRepo.findByTypeGroup(TypeGroup.ARCHIVE));
        return "personnel/profile";
    }

    @GetMapping(path = "/sigrh/agents/displayPhoto/{agentId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] displayPhoto(@PathVariable Long agentId)
    {
        if(agentId==null)
            return filesManager.downloadFile(ArchivageConstants.AGENT_UPLOADS_DIR + "\\PRF_PHT\\" +"m.jpg");
        Agent agent = agentRepo.findById(agentId).orElse(null);
        if(agent==null) return filesManager.downloadFile(ArchivageConstants.AGENT_UPLOADS_DIR + "\\PRF_PHT\\" +"m.jpg");
        if(agent.getNomPhoto() == null || !new File(agent.getNomPhoto()).exists())
        {
            return filesManager.downloadFile(ArchivageConstants.AGENT_UPLOADS_DIR + "\\PRF_PHT\\" + (agent.getCivilite() == Civility.MONSIEUR ? "m.jpg" : "f.png"));
        }
        return filesManager.downloadFile(agent.getNomPhoto());
    }
}
