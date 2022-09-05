package dgmp.sigrh.agentmodule.controller.services;

import dgmp.sigrh.agentmodule.model.dtos.CreateAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.ReadAgentDTO;
import dgmp.sigrh.agentmodule.model.dtos.UpdateAgentDTO;
import org.springframework.data.domain.Page;

public interface IAgentService
{
    ReadAgentDTO createAgent(CreateAgentDTO dto);
    ReadAgentDTO updateAgent(UpdateAgentDTO dto);

    Page<ReadAgentDTO> getAllAgentsPage(long strId);
    Page<ReadAgentDTO> getActiveAgentsPage(long strId); // Déduction faite des retraités, décédés et partis
    Page<ReadAgentDTO> getPresentAgentsPage(long strId); // Déduction faite de tous les absents y compris les non actifs

    Page<ReadAgentDTO> findAllAgentsPageByEmploi(long strId, long emploiId);
    Page<ReadAgentDTO> findActiveAgentsPage(long strId, long emploiId); // Déduction faite des retraités, décédés et partis
    Page<ReadAgentDTO> findPresentAgentsPage(long strId, long emploiId); // Déduction faite de tous les absents y compris les non actifs

    Page<ReadAgentDTO> findAllAgentsPageByFonction(long strId, long fonctionId);
    Page<ReadAgentDTO> findActiveAgentsPageByFonction(long strId, long fonctionId); // Déduction faite des retraités, décédés et partis
    Page<ReadAgentDTO> findPresentAgentsPageByFonction(long strId, long fonctionId); // Déduction faite de tous les absents y compris les non actifs

    Page<ReadAgentDTO> findAllAgentsPageByGrade(long strId, long gradeId);
    Page<ReadAgentDTO> findActiveAgentsPageByGrade(long strId, long gradeId); // Déduction faite des retraités, décédés et partis
    Page<ReadAgentDTO> findPresentAgentsPageByGrade(long strId, long gradeId); // Déduction faite de tous les absents y compris les non actifs

    Page<ReadAgentDTO> searchAllAgentsPage(long strId, String searchKey);
    Page<ReadAgentDTO> searchActiveAgentsPage(long strId, String searchKey); // Déduction faite des retraités, décédés et partis
    Page<ReadAgentDTO> searchPresentAgentsPage(long strId, String searchKey); // Déduction faite de tous les absents y compris les non actifs
}
