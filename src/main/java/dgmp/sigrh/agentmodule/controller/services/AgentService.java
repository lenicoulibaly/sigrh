package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.controller.exceptions.AgentAppException;
import dgmp.sigrh.agentmodule.controller.exceptions.AgentErrorMsg;
import dgmp.sigrh.agentmodule.controller.repositories.AgentHistoRepo;
import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.dtos.*;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.enums.Civility;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.agentmodule.model.enums.TypeAgent;
import dgmp.sigrh.agentmodule.model.histo.AgentHisto;
import dgmp.sigrh.archivemodule.controller.service.IArchiveService;
import dgmp.sigrh.archivemodule.model.dtos.ArchiveMapper;
import dgmp.sigrh.archivemodule.model.dtos.ArchiveReqDTO;
import dgmp.sigrh.archivemodule.model.entities.Archive;
import dgmp.sigrh.auth2.controller.services.spec.IUserService;
import dgmp.sigrh.auth2.model.dtos.appuser.CreateUserDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.ReadUserDTO;
import dgmp.sigrh.auth2.model.dtos.appuser.UserMapper;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.events.types.agent.AgentEventTypes;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiRepo;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionRepo;
import dgmp.sigrh.grademodule.controller.repositories.GradeRepo;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AgentService implements IAgentService
{
    private final AgentRepo agentRepo;
    private final AgentMapper agentMapper;
    private final UserMapper userMapper;
    private final StrRepo strRepo;
    private final IUserService userService;
    private final ISecurityContextManager scm;
    private final AgentHistoRepo agentHistoRepo;
    private final FonctionRepo fonctionRepo;
    private final EmploiRepo empRepo;
    private final GradeRepo gradeRepo;
    private final IArchiveService archiveService;
    private final ArchiveMapper archiveMapper;

    @Override
    public ReadAgentDTO createAgent(CreateNewAgentDTO dto) throws IllegalAccessException {
        Agent agent = agentMapper.mapToAgent(dto);
        agent = agentRepo.save(agent);
        AgentHisto agentHisto = agentMapper.mapToAgentHisto(agent, AgentEventTypes.CREATE_AGENT, scm.getEventActorIdFromSCM());
        agentHistoRepo.save(agentHisto);
        CreateUserDTO createUserDTO = userMapper.mapToCreateUserDTO(dto);
        userService.createUser(createUserDTO);
        return agentMapper.mapToReadAgentDTO(agent);
    }

    @Override @Transactional(rollbackFor = {Exception.class})
    public ReadAgentDTO registerAgent(RegisterAgentDTO dto) throws IllegalAccessException {
        Agent agent = agentMapper.mapToAgent(dto);
        agent.setFonction(fonctionRepo.getFonctionAgt());
        agent = agentRepo.save(agent);

        if(dto.getPhotoFile() != null)
        {
            if(dto.getPhotoFile().getOriginalFilename() != null)
            {
                if(!dto.getPhotoFile().getOriginalFilename().equals(""))
                {
                    ArchiveReqDTO archiveDTO = archiveMapper.mapToPhotoArchiveDTO(dto, agent.getAgentId());
                    Archive archive = archiveService.createArchive(archiveDTO);
                    agent.setNomPhoto(archive.getPath());
                }
            }
        }

        AgentHisto agentHisto = agentMapper.mapToAgentHisto(agent, AgentEventTypes.CREATE_AGENT, scm.getEventActorIdFromSCM());
        agentHistoRepo.save(agentHisto);
        CreateUserDTO createUserDTO = userMapper.mapToCreateUserDTO(dto);
        createUserDTO.setAgentId(agent.getAgentId());
        ReadUserDTO user = userService.createUser(createUserDTO);
        agent.setUser(new AppUser(user.getUserId()));
        return agentMapper.mapToReadAgentDTO(agent);
    }

    @Override
    public ReadAgentDTO updateAgent(UpdateAgentDTO dto)
    {
        Agent loadedAgent = agentRepo.findById(dto.getAgentId()).orElseThrow(()->new AgentAppException(AgentErrorMsg.AGENT_ID_NOT_FOUND_ERROR_MSG));
        BeanUtils.copyProperties(dto, loadedAgent);
        loadedAgent = agentRepo.save(loadedAgent);
        return agentMapper.mapToReadAgentDTO(loadedAgent);
    }

    @Override
    public Page<ReadAgentDTO> getAllAgentsPage(long strId) {
        return null;
    }

    @Override
    public List<ReadAgentDTO> getAllAgentsByStr(long strId, List<EtatRecrutement> etats)
    {
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        List<Agent> agents = agentRepo.getAgentsByStrAndEtat(strId, etats);
        List<ReadAgentDTO> readAgentDTOS = agents.stream().map(agentMapper::mapToReadAgentDTO).collect(Collectors.toList());
        return Stream.concat(readAgentDTOS.stream(), strRepo.getStrChildrenIds(strId).stream().flatMap(id->this.getAllAgentsByStr(id, etats).stream())).collect(Collectors.toList());
    }

    @Override
    public Page<ReadAgentDTO> getActiveAgentsPage(long strId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> getPresentAgentsPage(long strId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findAllAgentsPageByEmploi(long strId, long emploiId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findActiveAgentsPage(long strId, long emploiId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findPresentAgentsPage(long strId, long emploiId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findAllAgentsPageByFonction(long strId, long fonctionId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findActiveAgentsPageByFonction(long strId, long fonctionId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findPresentAgentsPageByFonction(long strId, long fonctionId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findAllAgentsPageByGrade(long strId, long gradeId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findActiveAgentsPageByGrade(long strId, long gradeId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> findPresentAgentsPageByGrade(long strId, long gradeId) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> searchAllAgentsPage(long strId, String searchKey) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> searchActiveAgentsPage(long strId, String searchKey, Pageable pageable)
    {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> searchPresentAgentsPage(long strId, String searchKey) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> searchAgentByStrAndEtat(long strId, List<EtatRecrutement> states, String searchKey, Pageable pageable)
    {
        Page<Agent> agentPage = searchKey == null ? agentRepo.findAgentByStrAndEtat(strId, states, pageable) :
                searchKey.trim().equals("") ? agentRepo.findAgentByStrAndEtat(strId, states, pageable) :
                agentRepo.searchAgentByStrAndEtat(strId, states, StringUtils.stripAccentsToUpperCase(searchKey), pageable);
        List<ReadAgentDTO> readAgentDTOS = agentPage.stream().map(agentMapper::mapToReadAgentDTO).collect(Collectors.toList());
        return new PageImpl<>(readAgentDTOS, pageable, agentPage.getTotalElements());
    }

    @Override
    public Page<ReadAgentDTO> searchAgents(Long visibilityId, String key, Pageable pageable)
    {
        Page<Agent> agents = agentRepo.searchAgents(visibilityId, StringUtils.stripAccentsToUpperCase(key).trim(), pageable);
        List<ReadAgentDTO> readAgentDTOList = agents.stream().map(agentMapper::mapToReadAgentDTO).collect(Collectors.toList());
        return new PageImpl<>(readAgentDTOList, pageable, agents.getTotalElements());
    }

    @Override
    public Page<ReadAgentDTO> searchAgentsMultiCriteres(Long visibilityId, String key, List<String> statesString,
                                                        List<String> civilitiesString, List<String> typeAgentsString,
                                                        List<Long> fonctionsIds, List<Long> gradesIds,
                                                        List<Long> emploisIds, Pageable pageable)
    {
        key = key==null ? "" : key;
        List<EtatRecrutement> states; List<Civility> civilities; List<TypeAgent> typeAgents; key = key == null ? "" : key;
        List<Long> fonctionsIds2 = new ArrayList<>(); List<Long> gradesIds2 = new ArrayList<>();List<Long> emploisIds2 = new ArrayList<>();

        states = statesString == null ? EtatRecrutement.getAll() : statesString.isEmpty()  || statesString.contains("") ? EtatRecrutement.getAll() : statesString.stream().map(EtatRecrutement::valueOf).collect(Collectors.toList());
        civilities = civilitiesString == null ? Civility.getAll() : civilitiesString.isEmpty() || civilitiesString.contains("") ? Civility.getAll() : civilitiesString.stream().map(Civility::valueOf).collect(Collectors.toList());
        typeAgents = typeAgentsString == null ? TypeAgent.getAll() : typeAgentsString.isEmpty() || typeAgentsString.contains("") ? TypeAgent.getAll() : typeAgentsString.stream().map(TypeAgent::valueOf).collect(Collectors.toList());

        fonctionsIds2 = fonctionsIds == null ? fonctionRepo.getActiveFonctionsIds(): fonctionsIds.isEmpty() || fonctionsIds.contains(null) ? fonctionRepo.getActiveFonctionsIds() : fonctionsIds;

        gradesIds2 = gradesIds == null ?
                gradeRepo.getActiveGradesIds(): gradesIds.isEmpty() || gradesIds.contains(null) ? gradeRepo.getActiveGradesIds() : gradesIds;
        emploisIds2 = emploisIds == null ? empRepo.getActiveEmploisIds(): emploisIds.isEmpty() || emploisIds.contains(null) ? empRepo.getActiveEmploisIds() : emploisIds;

        Page<Agent> agents = visibilityId==null ? new PageImpl<>(new ArrayList<>()) : agentRepo.searchAgentsMultiCriteres(visibilityId, StringUtils.stripAccentsToUpperCase(key), states, civilities, typeAgents, fonctionsIds2, gradesIds2, emploisIds2, pageable);

        List<ReadAgentDTO> readAgentDTOList = agents.stream().map(agentMapper::mapToReadAgentDTO).collect(Collectors.toList());
        return new PageImpl<>(readAgentDTOList, pageable, agents.getTotalElements());
    }
}