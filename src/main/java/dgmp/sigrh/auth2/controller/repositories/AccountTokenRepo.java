package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountTokenRepo extends JpaRepository<AccountToken, Long>
{
    boolean existsByToken(String token);
    Optional<AccountToken> findByToken(String token);

    @Query("select t from AccountToken t where t.token = ?1 and t.user.userId = ?2")
    boolean existsByTokenAndUserId(String token, Long userId);
}
