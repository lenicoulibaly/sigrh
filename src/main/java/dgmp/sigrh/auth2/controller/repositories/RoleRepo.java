package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RoleRepo extends JpaRepository<AppRole, Long>
{
    @Query("select r.roleName as roleName from AppRole r")
    Set<String> getRoleNames();

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1)")
    boolean alreadyExistsByName(String roleName);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1)")
    boolean alreadyExistsByCode(String roleCode);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleName) = upper(?1) and a.roleId <> ?2")
    boolean alreadyExistsByName(String roleName, Long roleId);

    @Query("select (count(a) > 0) from AppRole a where upper(a.roleCode) = upper(?1) and a.roleId <> ?2")
    boolean alreadyExistsByCode(String roleCode, Long roleId);

    @Query("select a from AppRole a where upper(function('strip_accents', a.roleCode)) like upper(concat('%', ?1, '%')) or upper(function('strip_accents', a.roleName)) like upper(concat('%', ?1, '%')) order by a.roleName")
    Page<AppRole> searchRoles(String searchKey, Pageable pageable);


}
