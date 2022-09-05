package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PrivilegeToUserAssRepo extends JpaRepository<PrivilegeToUserAss, Long>
{
    @Query("select (count(p) > 0) from PrivilegeToUserAss p where p.user.userId = ?1 and p.privilege.privilegeId = ?2 and p.structure.strId = ?3 and p.assStatus = 1 and coalesce(p.startsAt, current_time ) <= current_time and coalesce(p.endsAt, current_time ) >= current_time")
    boolean UserHasDirectPrivilegeAss(Long userId, Long privilegeId, Long strId);

    @Query("select p from PrivilegeToUserAss p where p.user.userId = ?1 and p.structure.strId = ?2 and p.assStatus = 1 and coalesce(p.startsAt, current_time ) <= current_time and coalesce(p.endsAt, current_time ) >= current_time")
    Set<PrivilegeToUserAss> getUsersDirectPrivilegeAss(Long userId, Long strId);

    @Query("select p from PrivilegeToUserAss p where p.user.userId = ?1 and p.structure.strId = ?2 and p.assStatus = 3")
    Set<PrivilegeToUserAss> getUsersRevokedPrivilegeAss(Long userId, Long strId);

    @Query("select a from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and a.assStatus = 1")
    Set<PrivilegeToUserAss> getImmediateAndNoneRevokedPrivilegesAssForUser(Long userId, Long strId);

    @Query("select (count(p) > 0) from PrivilegeToUserAss p where p.user.userId = ?1 and p.privilege.privilegeId = ?2 and p.structure.strId = ?3")
    boolean existsByUserAndPrvAndStr(Long userId, Long privilegeId, Long strId);

    @Query("select p from PrivilegeToUserAss p where p.user.userId = ?1 and p.privilege.privilegeId = ?2 and p.structure.strId = ?3")
    PrivilegeToUserAss findByUserAndPrvAndStr(Long userId, Long privilegeId, Long strId);

    @Query("select (count(p) > 0) from PrivilegeToUserAss p where p.user.userId = ?1 and p.privilege.privilegeId = ?2 and p.structure.strId = ?3 and p.assStatus = 3")
    boolean privilegeIsRevokedForUser(Long userId, Long privilegeId, Long strId);


}