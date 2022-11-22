package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.dtos.asignation.ReadPrincipalAssDTO;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PrincipalAssRepo extends JpaRepository<PrincipalAss, Long>
{
    @Query("select p from PrincipalAss p where p.user.userId= ?1")
    Set<PrincipalAss> findAllByUser(Long userId);

    @Query("select p from PrincipalAss p where p.user.userId= ?1 and p.ass.assStatus in (1, 2) and current_date between coalesce(p.ass.startsAt, current_date ) and coalesce(p.ass.endsAt, current_date)") //coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date
    Set<PrincipalAss> findNoneRevokedByUser(Long userId);

    @Query("select p from PrincipalAss p where p.user.userId= ?1 and p.ass.assStatus = 1 and coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date")
    Set<PrincipalAss> findActiveByUser(Long userId);

    @Query("select p.structure from PrincipalAss p where p.user.userId= ?1 and p.ass.assStatus = 1 and coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date")
    Set<Structure> findUserVisibility(Long userId);

    @Query("select (count(ps)>0) from PrincipalAss ps where ps.user.userId = ?1")
    boolean userHasAnyPrincipalAss(Long userId);

    @Query("select ps.user.userId from PrincipalAss ps where ps.assId = ?1")
    Long getUserId(Long assId);

    @Query("select new dgmp.sigrh.auth2.model.dtos.asignation.ReadPrincipalAssDTO(p.assId, p.intitule, p.ass.startsAt, p.ass.endsAt, p.ass.assStatus, case when p.ass.assStatus = 1 then 'Assignation courante' when p.ass.assStatus = 2 then 'Actif' when p.ass.assStatus = 3 then 'Inactif' else '' end, p.user.username, p.structure.strName, p.structure.strSigle, function('get_hierarchy_sigles', p.structure.strId) ) from PrincipalAss p where p.user.userId = ?1 and (locate(?2, upper(function('strip_accents', p.intitule)))>0 or locate(?2, upper(function('strip_accents', p.structure.strName)))>0 or locate(?2, upper(function('strip_accents', p.structure.strSigle)))>0 or (p.ass.assStatus = 1 and locate(?2, upper('Assignation courante'))>0)  or (p.ass.assStatus = 2 and locate(?2, upper('Actif'))>0)  or (p.ass.assStatus = 3 and locate(?2, upper('Inactif'))>0)) order by p.ass.assStatus asc")
    Page<ReadPrincipalAssDTO> searchPrincipalAssByUser(Long userId, String key, Pageable pageable);

    @Query("select (count(p.assId)=1) from PrincipalAss p where p.assId = ?1 and current_date between coalesce(p.ass.startsAt, current_date ) and coalesce(p.ass.endsAt, current_date)")
    boolean principalAssHasValidDates(Long assId);
}