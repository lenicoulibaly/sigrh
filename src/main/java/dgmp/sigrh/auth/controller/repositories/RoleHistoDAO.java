package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.histo.RoleHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHistoDAO extends JpaRepository<RoleHisto, Long> {
}