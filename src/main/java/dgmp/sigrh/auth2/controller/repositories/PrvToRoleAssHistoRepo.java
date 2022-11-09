package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.PrvToRoleAssHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrvToRoleAssHistoRepo extends JpaRepository<PrvToRoleAssHisto, Long> {
}