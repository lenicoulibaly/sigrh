package dgmp.sigrh.auth2.controller.repositories;

import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
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

}

