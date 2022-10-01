package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.controller.exceptions.AgentAppException;
import dgmp.sigrh.agentmodule.controller.exceptions.AgentErrorMsg;
import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.agentmodule.model.dtos.AgentMapper;
import dgmp.sigrh.agentmodule.model.dtos.CreateAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.ReadAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.UpdateAgentDTO;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.agentmodule.model.enums.EtatRecrutement;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AgentService implements IAgentService
{
    private final AgentDAO agentRepo;
    //private final IBrokerMessageSender<Agent> brokerMessageSender;
    private final AgentMapper agentMapper;
    private final StrRepo strRepo;
    @Override
    public ReadAgentDTO createAgent(CreateAgentDTO dto)
    {
        Agent agent = agentMapper.mapToAgent(dto);
        agent = agentRepo.save(agent);
        //brokerMessageSender.sendEvent(Topics.AGENT_TOPIC, AgentEventTypes.CREATE_AGENT, agent);
        return agentMapper.getReadAgentDTO(agent);
    }

    @Override
    public ReadAgentDTO updateAgent(UpdateAgentDTO dto)
    {
        Agent loadedAgent = agentRepo.findById(dto.getIdAgent()).orElseThrow(()->new AgentAppException(AgentErrorMsg.AGENT_ID_NOT_FOUND_ERROR_MSG));
        BeanUtils.copyProperties(dto, loadedAgent);
        loadedAgent = agentRepo.save(loadedAgent);
        return agentMapper.getReadAgentDTO(loadedAgent);
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
        List<ReadAgentDTO> readAgentDTOS = agents.stream().map(agentMapper::getReadAgentDTO).collect(Collectors.toList());
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
    public Page<ReadAgentDTO> searchActiveAgentsPage(long strId, String searchKey) {
        return null;
    }

    @Override
    public Page<ReadAgentDTO> searchPresentAgentsPage(long strId, String searchKey) {
        return null;
    }
}
