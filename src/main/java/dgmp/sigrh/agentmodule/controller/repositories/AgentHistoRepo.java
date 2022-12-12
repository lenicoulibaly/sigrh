package dgmp.sigrh.agentmodule.controller.repositories;

import dgmp.sigrh.agentmodule.model.histo.AgentHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentHistoRepo extends JpaRepository<AgentHisto, Long> {
}