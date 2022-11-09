package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.RoleAssHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAssHistoRepo extends JpaRepository<RoleAssHisto, Long> {
}