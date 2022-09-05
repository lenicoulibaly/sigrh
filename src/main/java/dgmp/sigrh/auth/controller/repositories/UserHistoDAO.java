package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.histo.UserHisto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoDAO extends JpaRepository<UserHisto, Long> {
}