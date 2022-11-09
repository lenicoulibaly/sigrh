package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.RoleAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RoleAssRepo extends JpaRepository<RoleAss, Long>
{
    @Query("select r from AppRole r where exists(select roleAss from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.role.roleId = r.roleId and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date)")
    Set<AppRole> getPrincipalAssRoles(Long principalAssId);

    @Query("select (count(r)>0) from AppRole r where exists(select roleAss from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.role.roleId = ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date)")
    boolean principalAssHasRole(Long principalAssId, Long roleId);

    @Query("select (count(r)>0) from AppRole r where exists(select roleAss from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.role.roleName = ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date)")
    boolean principalAssHasRole(Long principalAssId, String roleName);

    @Query("select (count(r)>0) from AppRole r where exists(select roleAss from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.role.roleId in ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date)")
    boolean principalAssHasAnyRole(Long principalAssId, List<Long> roleIds);

    @Query("select (count(r)>0) from AppRole r where exists(select roleAss from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.role.roleName in ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date)")
    boolean principalAssHasAnyRoleName(Long principalAssId, List<String> roleNames);

    @Query("select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1  and roleAss.role.roleId not in ?2")
    Set<Long> findRoleIdsForPrincipalAssNotIn(Long principalAssId, Set<Long> roleIdsToBeSet);

    @Query("select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1")
    Set<Long> findActiveRoleIdsBelongingToPrincipalAss(Long principalAssId);

    @Query("select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and roleAss.ass.startsAt = ?2 and roleAss.ass.startsAt = ?3")
    Set<Long> findActiveRoleIdsBelongingToPrincipalAss_samDates(Long principalAssId, LocalDate startsAt, LocalDate endsAt);

    @Query("select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and (roleAss.ass.startsAt <> ?2 or roleAss.ass.startsAt <> ?3)")
    Set<Long> findActiveRoleIdsBelongingToPrincipalAss_otherDates(Long principalAssId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.roleId from AppRole r where not exists( select r2 from RoleAss r2 where r2.principalAss.assId = ?1 and r2.role.roleId = r.roleId)")
    Set<Long> findRoleIdsNotBelongingToPrincipalAss(Long principalAssId);

    @Query("select r.role.roleId from RoleAss r where r.principalAss.assId = ?1 and r.ass.assStatus <> 1")
    Set<Long> findNoneActiveRoleIdsBelongingToPrincipalAss(Long principalAssId);

    @Query("select r.role.roleId from RoleAss r where r.principalAss.assId = ?1 and r.ass.assStatus <> 1 and r.ass.startsAt = ?2 and r.ass.startsAt = ?3")
    Set<Long> findNoneActiveRoleIdsBelongingToPrincipalAss_sameDates(Long principalAssId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.role.roleId from RoleAss r where r.principalAss.assId = ?1 and r.ass.assStatus <> 1 and (r.ass.startsAt <> ?2 or r.ass.startsAt <> ?3)")
    Set<Long> findNoneActiveRoleIdsBelongingToPrincipalAss_otherDates(Long principalAssId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r from RoleAss r where r.principalAss.assId = ?1 and r.role.roleId = ?2")
    RoleAss findByPrincipalAssAndRole(Long principalAssId, Long roleId);
}