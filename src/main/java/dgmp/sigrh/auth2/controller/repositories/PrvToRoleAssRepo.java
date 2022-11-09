package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.PrvToRoleAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PrvToRoleAssRepo extends JpaRepository<PrvToRoleAss, Long>
{
    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    boolean existsByRoleAndPrivilege(Long roleId, Long privilegeId);

    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2 and p.ass.assStatus = ?3")
    boolean existsByRoleAndPrivilege(Long roleId, Long privilegeId, int assStatus);

    @Query("select p from PrvToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    PrvToRoleAss findByRoleAndPrivilege(Long roleId, Long prvId);

    @Query("select p from PrvToRoleAss p where p.role.roleId = ?1")
    List<PrvToRoleAss> findByRole(Long roleId);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.ass.assStatus = 1  and coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date")
    Set<Long> findActivePrvIdsForRole(Long roleId);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId in ?1 and p.ass.assStatus = 1  and coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date")
    Set<Long> findActivePrvIdsForRoles(Set<Long> roleIds);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.ass.assStatus = 1  and p.privilege.privilegeId not in ?2")
    Set<Long> findPrvIdsForRoleNotIn(Long roleId, Set<Long> prvIdsToBeSet);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where (p.role.roleId <> ?1 or (p.role.roleId = ?1 and (p.ass.assStatus <>1 or coalesce(p.ass.startsAt, current_date) > current_date or coalesce(p.ass.endsAt, current_date) < current_date ))) and p.privilege.privilegeId in ?2")
    Set<Long> findPrvIdsNotBelongingToRoleIn(Long roleId, Set<Long> prvIdsToBeSet);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.ass.assStatus = 1 and p.privilege.privilegeId in ?2 and coalesce(p.ass.startsAt, ?3) = ?3 and coalesce(p.ass.endsAt, ?4) = ?4 and coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date")
    Set<Long> findActivePrvIdsForRoleIn_sameDates(Long roleId, Set<Long> prvIdsToBeSet, LocalDate startsAt, LocalDate endsAt);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.ass.assStatus = 1 and p.privilege.privilegeId in ?2 and p.ass.startsAt <> ?3 and p.ass.endsAt <> ?4")
    Set<Long> findActivePrvIdsForRoleIn_otherDates(Long roleId, Set<Long> prvIdsToBeSet, LocalDate startsAt, LocalDate endsAt);

    @Query("select p.privilege.privilegeId from PrvToRoleAss p where p.role.roleId = ?1 and p.ass.assStatus <> 1 and p.privilege.privilegeId in ?2")
    Set<Long> findNoneActivePrvIdsForRoleIn(Long roleId, Set<Long> prvIdsToBeSet);

}