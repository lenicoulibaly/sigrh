package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleDAO extends JpaRepository<AppRole, Long>
{
    @Query("select r.roleName as roleName from AppRole r")
    Set<String> getRoleNames();

    @Query("select (count(a)>0) from RoleToUserAss a where a.user.userId = ?1 and a.role.roleId = ?2 and a.structure.strId = ?3 and a.assStatus = 1 and coalesce(a.startsAt, current_date ) <= current_date and coalesce(a.endsAt, current_date) >= current_date")
    boolean hasValidAndDefaultRole(Long userId, Long roleId, Long strId);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1)")
    boolean alreadyExistsByName(String roleName);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1)")
    boolean alreadyExistsByCode(String roleCode);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1) and a.roleId <> ?2")
    boolean alreadyExistsByName(String roleName, Long roleId);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1) and a.roleId <> ?2")
    boolean alreadyExistsByCode(String roleCode, Long roleId);

    @Query("select a from AppRole a where upper(a.roleCode) like upper(concat('%', ?1, '%')) or upper(a.roleName) like upper(concat('%', ?1, '%')) order by a.roleName")
    Page<AppRole> searchRoles(String searchKey, Pageable pageable);
}
