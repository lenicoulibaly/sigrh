package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.histo.AssignationHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignationHistoDAO extends JpaRepository<AssignationHisto, Long> {
}