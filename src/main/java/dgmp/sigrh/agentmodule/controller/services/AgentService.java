package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.controller.exceptions.AgentAppException;
import dgmp.sigrh.agentmodule.controller.exceptions.AgentErrorMsg;
import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.agentmodule.model.dtos.AgentMapper;
import dgmp.sigrh.agentmodule.model.dtos.CreateAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.ReadAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.UpdateAgentDTO;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService implements IAgentService
{
    private final AgentDAO agentDAO;
    //private final IBrokerMessageSender<Agent> brokerMessageSender;
    private final AgentMapper agentMapper;
    @Override
    public ReadAgentDTO createAgent(CreateAgentDTO dto)
    {
        Agent agent = agentMapper.mapToAgent(dto);
        agent = agentDAO.save(agent);
        //brokerMessageSender.sendEvent(Topics.AGENT_TOPIC, AgentEventTypes.CREATE_AGENT, agent);
        return agentMapper.getReadAgentDTO(agent);
    }

    @Override
    public ReadAgentDTO updateAgent(UpdateAgentDTO dto)
    {
        Agent loadedAgent = agentDAO.findById(dto.getIdAgent()).orElseThrow(()->new AgentAppException(AgentErrorMsg.AGENT_ID_NOT_FOUND_ERROR_MSG));
        BeanUtils.copyProperties(dto, loadedAgent);
        loadedAgent = agentDAO.save(loadedAgent);
        return agentMapper.getReadAgentDTO(loadedAgent);
    }

    @Override
    public Page<ReadAgentDTO> getAllAgentsPage(long strId) {
        return null;
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
