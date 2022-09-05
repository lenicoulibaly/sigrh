package dgmp.sigrh.auth.controller.repositories;

import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RoleToUserAssRepo extends JpaRepository<RoleToUserAss, Long>
{
    @Query("select r from RoleToUserAss r where r.user.userId = ?1 and r.assStatus = 1 and coalesce(r.startsAt, current_date) <= current_date and coalesce(r.endsAt, current_date) >= current_date")
    List<RoleToUserAss> getUsersActiveRoleAss(Long userId);

    @Query("select a from RoleToUserAss a where a.user.userId = ?1 and a.assStatus in (1, 2) and coalesce(a.startsAt, current_date) <= current_date and coalesce(a.endsAt, current_date) >= current_date")
    Set<RoleToUserAss> getUsersRoleAss(Long userId);

    @Query("select r from RoleToUserAss r where r.user.userId = ?1 and r.assStatus in (1, 2)")
    List<RoleToUserAss> getUsersNoneRevokedRoleAss(Long userId);

    @Query("select a as role from RoleToUserAss a where a.user.userId = :userId and (coalesce(a.startsAt, current_date) > current_date or coalesce(a.endsAt, current_date) < current_date)")
    Set<RoleToUserAss> getUsersExpiredRoleAss(@Param("userId") Long userId);


    @Query("select (count(r)>0)  from RoleToUserAss r where r.user.userId = ?1 and r.assStatus = 1 and coalesce(r.startsAt, current_date) <= current_date and coalesce(r.endsAt, current_date) >= current_date")
    boolean userHasAnActiveRoleAss(Long userId);

    @Query("select (count(r) > 0) from RoleToUserAss r where r.user.userId = ?1 and r.role.roleId = ?2 and r.structure.strId = ?3 and r.assStatus = 1 and coalesce(r.startsAt, current_date) <= current_date and coalesce(r.endsAt, current_date) >= current_date")
    boolean userHasRole(Long userId, Long roleId, Long strId);

    @Query("select (count(r) > 0) from RoleToUserAss r where r.user.userId = ?1 and r.role.roleId = ?2 and r.structure.strId = ?3 and r.assStatus = 3")
    boolean roleIsRevokedForUser(Long userId, Long roleId, Long strId);

    @Query("select (count(r) > 0) from RoleToUserAss r where r.user.userId = ?1 and r.role.roleId = ?2 and r.structure.strId = ?3")
    boolean existsByUserAndRoleAndStr(Long userId, Long roleId, Long strId);

    @Query("select r from RoleToUserAss r where r.user.userId = ?1 and r.role.roleId = ?2 and r.structure.strId = ?3")
    RoleToUserAss findByUserAndRoleAndStr(Long userId, Long roleId, Long strId);

    @Modifying
    @Query("update RoleToUserAss rtu set rtu.assStatus = 2 where rtu.user.userId = ?1 and rtu.assStatus = 1")
    void setDefaultAssAsNoneDefault(Long userId);
}