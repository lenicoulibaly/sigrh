package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.RoleHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHistoRepo extends JpaRepository<RoleHisto, Long> {
}