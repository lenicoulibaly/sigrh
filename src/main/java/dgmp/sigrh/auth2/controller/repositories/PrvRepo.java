package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PrvRepo extends JpaRepository<AppPrivilege, Long>
{
    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByName(String privilegeName, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeName) = upper(?1)")
    boolean alreadyExistsByName(String privilegeName);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1) and a.privilegeId <> ?2")
    boolean alreadyExistsByCode(String privilegeCode, Long privilegeId);

    @Query("select (count(a) > 0) from AppPrivilege a where upper(a.privilegeCode) = upper(?1)")
    boolean alreadyExistsByCode(String privilegeCode);

    @Query("select (count(a) > 0) from PrvToRoleAss a where a.role.roleId = ?1 and a.privilege.privilegeId = ?2 and a.ass.assStatus = 1 and coalesce(a.ass.startsAt, current_date ) <= current_date and coalesce(a.ass.endsAt, current_date) >= current_date")
    boolean roleHasValidPrivilege(Long roleId, Long privilegeId);




    @Query("select p from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)")
    Set<AppPrivilege> getRolePrivileges(long roleId);

    @Query("select (count(p)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId = ?2 and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)")
    boolean roleHasPrivilege(long roleId, long prvId);

    @Query("select (count(p)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleId = ?1 and ptrAss.privilege.privilegeId in ?2 and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)")
    boolean roleHasAnyPrivilege(long roleId, List<Long> prvIds);

    @Query("select a from AppPrivilege a where upper(function('strip_accents', a.privilegeCode) ) like upper(concat('%', function('strip_accents', ?1) , '%')) or upper(function('strip_accents', a.privilegeName)) like upper(concat('%', function('strip_accents', ?1), '%'))")
    Page<AppPrivilege> searchPrivileges(String searchKey, Pageable pageable);
    @Query("select a from AppPrivilege a where upper(function('strip_accents', a.privilegeCode) ) like upper(concat('%', function('strip_accents', ?1) , '%')) or upper(function('strip_accents', a.privilegeName)) like upper(concat('%', function('strip_accents', ?1), '%'))")
    Set<AppPrivilege> searchPrivileges(String searchKey);

    @Query("select p.privilegeName from AppPrivilege p")
    Set<String> getPrivilegeNames();

    AppPrivilege findByPrivilegeCode(String privilegeCode);
}