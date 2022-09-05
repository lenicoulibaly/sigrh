package dgmp.sigrh.emploimodule.controller.repositories;

import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmploiHistoDAO extends JpaRepository<EmploiHisto, Long>
{
    @Query("select e from EmploiHisto e where e.idEmploi = ?1")
    List<EmploiHisto> getModificationsList(Long idEmploi);

    @Query("select e from EmploiHisto e where e.idEmploi = ?1 order by e.eventActorIdentifier.modificationDate DESC")
    Page<EmploiHisto> getHistoPageBetweenPeriod(Long idEmploi, Pageable pageable);

    @Query("select e from EmploiHisto e where e.idEmploi = ?1 and e.eventActorIdentifier.modificationDate >= ?2 and e.eventActorIdentifier.modificationDate <= ?3 order by e.eventActorIdentifier.modificationDate DESC")
    Page<EmploiHisto> getHistoPageBetweenPeriod(Long idEmploi, LocalDateTime after, LocalDateTime before, Pageable pageable);

    @Query("select e from EmploiHisto e where e.idEmploi = ?1 and e.eventActorIdentifier.modifierUsername = ?2 and e.eventActorIdentifier.modificationDate >= ?3 and e.eventActorIdentifier.modificationDate <= ?4 order by e.eventActorIdentifier.modificationDate DESC")
    Page<EmploiHisto> getHistoPageBetweenPeriod(Long idEmploi, String username, LocalDateTime after, LocalDateTime before, Pageable pageable);

    @Query("select (count(eh)>0) from EmploiHisto eh where eh.idEmploi = ?1")
    long countByIdEmploi(long idEmploi);
}