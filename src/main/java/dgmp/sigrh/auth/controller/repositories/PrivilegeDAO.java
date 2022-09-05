package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PrivilegeDAO extends JpaRepository<AppPrivilege, Long>
{
    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByName(String privilegeName, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1)")
    boolean alreadyExistsByName(String privilegeName);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByCode(String privilegeCode, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1)")
    boolean alreadyExistsByCode(String privilegeCode);

    @Query("select (count(a) > 0) from PrivilegeToUserAss a where a.user.userId = ?1 and a.privilege.privilegeId = ?2 and a.structure.strId = ?3 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= ?4 and coalesce(a.endsAt, current_date) >= ?5")
    boolean hasImmediateAndValidPrivilege(Long userId, Long privilegeId, Long strId);

    @Query("select (count(a) > 0) from PrivilegeToRoleAss a where a.role.roleId = ?1 and a.privilege.privilegeId = ?2 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= current_date and coalesce(a.endsAt, current_date) >= current_date ")
    boolean roleHasValidPrivilege(Long roleId, Long privilegeId);

    @Query("select (count(a) > 0) from PrivilegeToUserAss a where a.user.userId = ?1 and a.privilege.privilegeId = ?2 and a.structure.strId = ?3 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= current_date and coalesce(a.endsAt, current_date) >= current_date")
    boolean isPrivilegeValidForUser(Long userId, Long privilegeId, Long strId);

    @Query("select (count(a) > 0) from PrivilegeToUserAss a where a.user.userId = ?1 and a.privilege.privilegeId = ?2 and a.structure.strId = ?3 and a.assStatus = 1")
    boolean isPrivilegeNotRevokedForUser(Long userId, Long privilegeId, Long strId);

    @Query("select a from AppPrivilege a where upper(a.privilegeCode) like upper(concat('%', ?1, '%')) or upper(a.privilegeName) like upper(concat('%', ?1, '%'))")
    Page<AppPrivilege> searchPrivileges(String searchKey, Pageable pageable);
    @Query("select a from AppPrivilege a where upper(a.privilegeCode) like upper(concat('%', ?1, '%')) or upper(a.privilegeName) like upper(concat('%', ?1, '%'))")
    Set<AppPrivilege> searchPrivileges(String searchKey);

    @Query("select a.privilege from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= current_date and coalesce(a.endsAt, current_date) >= current_date")
    Set<AppPrivilege> getImmediatePrivilegesForUser(Long userId, Long strId);

    @Query("select a.privilege from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and a.assStatus = 1")
    Set<AppPrivilege> getImmediateAndNoneRevokedPrivilegesForUser(Long userId, Long strId);

    @Query("select a.privilege from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and a.assStatus = 3")
    Set<AppPrivilege> getRevokedPrivilegesForUser(Long userId, Long strId);

    @Query("select a.privilege as privilege from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and (coalesce(a.startsAt, current_date ) > current_date or coalesce(a.endsAt, current_date ) < current_date)")
    Set<AppRole> getExpiredPrivilegesForUser(Long userId, Long strId);

    @Query("select a.privilege from PrivilegeToUserAss a where a.user.userId = ?1 and a.structure.strId = ?2 and a.assStatus in (2, 3)  and (coalesce(a.startsAt, current_date ) > current_date or coalesce(a.endsAt, current_date) < current_date)")
    Set<AppRole> getInvalidPrivilegesForUser(Long userId, Long strId);

    @Query("select a.privilege from PrivilegeToRoleAss a where a.role.roleId = ?1 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= current_date and coalesce(a.endsAt, current_date) >= current_date ")
    Set<AppPrivilege> getValidPrivilegesForRole(Long roleId);

    @Query("select a.privilege from PrivilegeToRoleAss a where a.role.roleId = ?1 and a.assStatus = 3")
    Set<AppPrivilege> getRevokedPrivilegesForRole(Long roleId);

    @Query("select a.privilege from PrivilegeToRoleAss a where a.role.roleId = ?1 and (coalesce(a.startsAt, current_date ) > current_date or coalesce(a.endsAt, current_date) < current_date)")
    Set<AppPrivilege> getExpiredPrivilegesForRole(Long roleId);

    @Query("select a.privilege from PrivilegeToRoleAss a where a.role.roleId = ?1 and (a.assStatus in (2, 3) or coalesce(a.startsAt, current_date ) > current_date or coalesce(a.endsAt, current_date) < current_date)")
    Set<AppPrivilege> getInvalidPrivilegesForRole(Long roleId);

    @Query("select p.privilegeName from AppPrivilege p")
    Set<String> getPrivilegeNames();

    AppPrivilege findByPrivilegeCode(String privilegeCode);
}