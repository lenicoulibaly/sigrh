package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.histo.PrivilegeHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeHistoDAO extends JpaRepository<PrivilegeHisto, Long> {
}