package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTokenDAO extends JpaRepository<AccountToken, Long>
{
    boolean existsByToken(String token);
    Optional<AccountToken> findByToken(String token);
}
