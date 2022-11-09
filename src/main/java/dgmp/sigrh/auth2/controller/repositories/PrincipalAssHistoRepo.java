package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.PrincipalAssHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalAssHistoRepo extends JpaRepository<PrincipalAssHisto, Long>
{
}
