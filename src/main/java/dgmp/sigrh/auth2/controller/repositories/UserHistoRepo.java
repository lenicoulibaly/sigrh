package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.histo.UserHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoRepo extends JpaRepository<UserHisto, Long> {
}