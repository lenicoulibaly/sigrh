package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.PrvAss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PrvAssRepo extends JpaRepository<PrvAss, Long>
{
    @Query("select p from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeId = p.privilegeId and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and current_date between coalesce(prvAss.ass.startsAt, current_date ) and coalesce(prvAss.ass.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and current_date between coalesce(roleAss.ass.startsAt, current_date ) and coalesce(roleAss.ass.endsAt, current_date)) and ptrAss.ass.assStatus = 1 and current_date between coalesce(ptrAss.ass.startsAt, current_date ) and coalesce(ptrAss.ass.endsAt, current_date))) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where p.privilegeId = prvAss2.prv.privilegeId and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    Set<AppPrivilege> getPrincipalAssPrivileges(Long principalAssId);

    @Query("select p from AppPrivilege p where (locate(?2, upper(function('strip_accents', p.privilegeName)) ) > 0 or locate(?2, upper(function('strip_accents', p.privilegeCode))) > 0) and (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeId = p.privilegeId and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and current_date between coalesce(prvAss.ass.startsAt, current_date ) and coalesce(prvAss.ass.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and current_date between coalesce(roleAss.ass.startsAt, current_date ) and coalesce(roleAss.ass.endsAt, current_date)) and ptrAss.ass.assStatus = 1 and current_date between coalesce(ptrAss.ass.startsAt, current_date ) and coalesce(ptrAss.ass.endsAt, current_date))) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where p.privilegeId = prvAss2.prv.privilegeId and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    Page<PrvAss> searchActivePrvsByPrincipalAss(Long principalAssId, String key, Pageable pageable);

    @Query("select p.privilegeId from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeId = p.privilegeId and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and current_date between coalesce(prvAss.ass.startsAt, current_date ) and coalesce(prvAss.ass.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = p.privilegeId and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and current_date between coalesce(roleAss.ass.startsAt, current_date ) and coalesce(roleAss.ass.endsAt, current_date)) and ptrAss.ass.assStatus = 1 and current_date between coalesce(ptrAss.ass.startsAt, current_date ) and coalesce(ptrAss.ass.endsAt, current_date))) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where p.privilegeId = prvAss2.prv.privilegeId and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    Set<Long> getPrincipalAssPrvIds(Long principalAssId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = ?2 and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and current_date between coalesce(roleAss.ass.startsAt, current_date ) and coalesce(roleAss.ass.endsAt, current_date)) and ptrAss.ass.assStatus = 1 and current_date between coalesce(ptrAss.ass.startsAt, current_date ) and coalesce(ptrAss.ass.endsAt, current_date)))")
    boolean anyPrincipalAssRoleHasPrivilegeId(Long principalAssId, Long prvId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeId = ?2 and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and current_date between coalesce(prvAss.ass.startsAt, current_date ) and coalesce(prvAss.ass.endsAt, current_date)) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId = ?2 and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and current_date between coalesce(roleAss.ass.startsAt, current_date ) and coalesce(roleAss.ass.endsAt, current_date)) and ptrAss.ass.assStatus = 1 and current_date between coalesce(ptrAss.ass.startsAt, current_date ) and coalesce(ptrAss.ass.endsAt, current_date))) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where ?2 = prvAss2.prv.privilegeId and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    boolean principalAssHasPrivilegeId(Long principalAssId, Long prvId);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeCode = ?2 and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and coalesce(prvAss.ass.startsAt, current_date ) <= current_date and coalesce(prvAss.ass.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeCode = ?2 and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date) and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where ?2 = prvAss2.prv.privilegeCode and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    boolean principalAssHasPrivilegeCode(Long principalAssId, String prvCode);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeId in ?2 and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and coalesce(prvAss.ass.startsAt, current_date ) <= current_date and coalesce(prvAss.ass.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeId in ?2 and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date) and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where prvAss2.prv.privilegeId in ?2  and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    boolean principalAssHasAnyPrivilege(Long principalAssId, List<Long> prvIds);

    @Query("select (count(p)>0) from AppPrivilege p where (" +
            "exists (select prvAss from PrvAss prvAss where prvAss.prv.privilegeName in ?2 and prvAss.principalAss.assId = ?1 and prvAss.ass.assStatus = 1 and coalesce(prvAss.ass.startsAt, current_date ) <= current_date and coalesce(prvAss.ass.endsAt, current_date) >= current_date) or " +
            "exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeName in ?2 and ptrAss.role.roleId in (select roleAss.role.roleId from RoleAss roleAss where roleAss.principalAss.assId = ?1 and roleAss.ass.assStatus = 1 and coalesce(roleAss.ass.startsAt, current_date ) <= current_date and coalesce(roleAss.ass.endsAt, current_date) >= current_date) and ptrAss.ass.assStatus = 1 and coalesce(ptrAss.ass.startsAt, current_date ) <= current_date and coalesce(ptrAss.ass.endsAt, current_date) >= current_date)) and " +
            "not exists (select prvAss2 from PrvAss prvAss2 where prvAss2.prv.privilegeName in ?2  and prvAss2.ass.assStatus = 3 and prvAss2.principalAss.assId = ?1)")
    boolean principalAssHasAnyPrivilegeCode(Long principalAssId, List<String> prvCodes);

    @Query("select p from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2")
    PrvAss findByPrincipalAssAndPrv(Long principalAssId, Long privilegeId);

    @Query("select p from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus = ?3")
    PrvAss findByPrincipalAssAndPrvAndStatus(Long principalAssId, Long privilegeId, int assStatus);

    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2")
    boolean existsByPrincipalAssAndPrv(Long principalAssId, Long privilegeId);

    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus = ?3")
    boolean existsByPrincipalAssAndPrvAndStatus(Long principalAssId, Long privilegeId, int assStatus);

    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus = 1 and (coalesce(p.ass.startsAt, current_date) <> coalesce(?3, current_date) or coalesce(p.ass.endsAt, current_date) <> coalesce(?4, current_date))")
    boolean existsActiveByPrincipalAssAndPrv_OtherDates(Long principalAssId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);


    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus = 1 and (coalesce(p.ass.startsAt, current_date) = coalesce(?3, current_date) and coalesce(p.ass.endsAt, current_date) = coalesce(?4, current_date))")
    boolean existsActiveByPrincipalAssAndPrv_SameDates(Long principalAssId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);

    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus <> 1 and (coalesce(p.ass.startsAt, current_date) <> coalesce(?3, current_date) or coalesce(p.ass.endsAt, current_date) <> coalesce(?4, current_date))")
    boolean existsNoneActiveByPrincipalAssAndPrv_OtherDates(Long principalAssId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);

    @Query("select (count(p.assId)>0) from PrvAss p where p.principalAss.assId = ?1 and p.prv.privilegeId = ?2 and p.ass.assStatus <> 1 and (coalesce(p.ass.startsAt, current_date) = coalesce(?3, current_date) and coalesce(p.ass.endsAt, current_date) = coalesce(?4, current_date))")
    boolean existsNoneActiveByPrincipalAssAndPrv_SameDates(Long principalAssId, Long privilegeId, LocalDate startsAt, LocalDate endsAt);
}