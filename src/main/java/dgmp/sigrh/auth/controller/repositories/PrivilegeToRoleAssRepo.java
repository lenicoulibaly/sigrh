package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.PrivilegeToRoleAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PrivilegeToRoleAssRepo extends JpaRepository<PrivilegeToRoleAss, Long>
{
    @Query("select p from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.assStatus = 1 and coalesce(p.startsAt, current_time ) <= current_time and coalesce(p.endsAt, current_time ) >= current_time ")
    Set<PrivilegeToRoleAss> getPrivilegeAssForRole(Long roleId);

    @Query("select p from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2 and p.assStatus in (1, 2) and coalesce(p.startsAt, current_time ) <= current_time and coalesce(p.endsAt, current_time ) >= current_time")
    Set<PrivilegeToRoleAss> getNoneRevokedPrivilegeAssForRole(Long roleId, Long privilegeId);

    @Query("select p from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = 3")
    Set<PrivilegeToRoleAss> getRevokedPrivilegeAssForRole(Long roleId, Long privilegeId);

    @Query("select (count(p) > 0) from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2 and p.assStatus = 1 and coalesce(p.startsAt, current_time ) <= current_time and coalesce(p.endsAt, current_time ) >= current_time")
    boolean roleHasPrivilege(Long roleId, Long privilegeId);

    @Query("select (count(p) > 0) from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    boolean existsByRoleAndPrv(Long roleId, Long privilegeId);

    @Query("select p from PrivilegeToRoleAss p where p.role.roleId = ?1 and p.privilege.privilegeId = ?2")
    PrivilegeToRoleAss findByRoleAndPrv(Long roleId, Long privilegeId);
}