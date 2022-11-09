package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.PrvAssHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrvAssHistoRepo extends JpaRepository<PrvAssHisto, Long> {
}