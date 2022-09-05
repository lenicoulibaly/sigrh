package dgmp.sigrh.fonctionmodule.controller.repositories;

import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FonctionHistoDAO extends JpaRepository<FonctionHisto, Long>
{
    @Query("select f from FonctionHisto f where f.idFonction = ?1")
    List<FonctionHisto> getModificationsList(Long idFonction);

    @Query("select f from FonctionHisto f where f.idFonction = ?1 and f.eventActorIdentifier.modificationDate >= ?2 and f.eventActorIdentifier.modificationDate <= ?3 order by f.eventActorIdentifier.modificationDate DESC")
    Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, LocalDateTime after, LocalDateTime before, PageRequest of);

    @Query("select f from FonctionHisto f where f.idFonction = ?1 and f.eventActorIdentifier.modifierUsername = ?2 and f.eventActorIdentifier.modificationDate >= ?3 and f.eventActorIdentifier.modificationDate <= ?4 order by f.eventActorIdentifier.modificationDate DESC")
    Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, String username, LocalDateTime after, LocalDateTime before, PageRequest of);
}