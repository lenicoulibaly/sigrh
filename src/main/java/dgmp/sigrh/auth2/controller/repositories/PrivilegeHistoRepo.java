package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.PrivilegeHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeHistoRepo extends JpaRepository<PrivilegeHisto, Long> {
}