package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<AppUser, Long>
{
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByTel(String tel);

    boolean existsByUserIdAndUsername(Long userId, String username);

    @Query("select u.userId from AppUser u where u.username = ?1")
    Long getUserIdByUsername(String username);

    @Query("select u from AppUser u left join Structure s on u.structure.strId = s.strId where locate(concat(function('getStrCode', coalesce(?1, s.strId)), '/') , s.strCode) = 1 or coalesce(?1, s.strId) = s.strId ")
    Page<AppUser> findUserUnderStr(Long strId, Pageable pageable);

    @Query("select u from AppUser u left join Structure s on u.structure.strId = s.strId where locate(concat(coalesce(?1, s.strCode) , '/'), s.strCode) = 1 or coalesce(?1, s.strCode) = s.strCode")
    Page<AppUser> findUserUnderStr(String strCode, Pageable pageable);

    @Query("select a from AppUser a order by a.creationDate DESC")
    Page<AppUser> getUsersPage(Pageable pageable);

    @Query("select u from AppUser u left join Structure s on u.structure.strId = s.strId where upper(function('strip_accents', u.username)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', u.email)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', u.tel)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', s.strName)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', s.strSigle)) like upper(concat('%', ?1, '%')) order by u.creationDate DESC")
    Page<AppUser> searchUsers(String searchKey, Pageable pageable);

    @Query("select u from AppUser u left join Structure s on u.structure.strId = s.strId where (upper(function('strip_accents', u.username)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', u.email)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', u.tel)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', s.strName)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', s.strSigle)) like upper(concat('%', ?1, '%')) )  and (locate(concat(function('getStrCode', coalesce(?2, s.strId)), '/') , s.strCode) = 1 or coalesce(?2, s.strId) = s.strId ) order by u.creationDate DESC")
    Page<AppUser> searchUsersUnderStr(String searchKey, Long strId, Pageable pageable);//------->

    @Query("select u from AppUser u left join Structure s on u.structure.strId = s.strId where (locate(concat(function('getStrCode', coalesce(?1, s.strId)), '/') , s.strCode) = 1 or coalesce(?1, s.strId) = s.strId ) order by u.username ASC")
    List<AppUser> findAllUsersUnderStr(Long strId);

    @Query("select a from AppUser a where a.structure.strId = ?1 order by a.creationDate")
    Page<AppUser> findByStructure(Long strId, Pageable pageable);

    @Query("select a from AppUser a where a.structure.strId = ?1 order by a.creationDate")
    List<AppUser> findByStructure(Long strId);

    @Query("select (count(a) > 0) from AppUser a where upper(a.username) = upper(?1)")
    boolean alreadyExistsByUsername(String username);

    @Query("select (count(a) > 0) from AppUser a where upper(a.username) = upper(?1) and a.userId <> ?2")
    boolean alreadyExistsByUsername(String username, Long userId);

    @Query("select (count(t) > 0) from AccountToken t where upper(t.user.username) <> upper(?1) and t.token = ?2")
    boolean alreadyExistsByUsernameAndToken(String username, String token);

    @Query("select (count(t) > 0) from AccountToken t where t.user.userId <> ?1 and t.token = ?2")
    boolean alreadyExistsByUsernameAndToken(Long UserId, String token);



    @Query("select (count(a) > 0) from AppUser a where upper(a.email) = upper(?1)")
    boolean alreadyExistsByEmail(String email);

    @Query("select (count(a) > 0) from AppUser a where a.email = ?1 and a.userId <> ?2")
    boolean alreadyExistsByEmail(String email, Long userId);

    @Query("select (count(a) > 0) from AppUser a where upper(a.tel) = upper(?1)")
    boolean alreadyExistsByTel(String tel);

    @Query("select (count(a) > 0) from AppUser a where upper(a.tel) = upper(?1) and a.userId <> ?2")
    boolean alreadyExistsByTel(String tel, Long userId);

    @Query("update AppUser u set u.defaultAssId = ?2 where u.userId = ?1")
    @Modifying
    void changeDefaultAssId(Long userId, Long assId);

    @Query("select u.username from AppUser u where  u.userId = ?1")
    String getUsername(Long userId);

}
