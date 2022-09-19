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

    @Query("select f from FonctionHisto f where f.idFonction = ?1 and f.eai.modificationDate <= ?2 and f.eai.modificationDate >= ?3 order by f.eai.modificationDate DESC")
    Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, LocalDateTime before, LocalDateTime after, PageRequest of);

    @Query("select f from FonctionHisto f where f.idFonction = ?1 and f.eai.modifierUsername = ?2 and f.eai.modificationDate <= ?3 and f.eai.modificationDate >= ?4 order by f.eai.modificationDate DESC")
    Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, String username, LocalDateTime before, LocalDateTime after, PageRequest of);

    @Query("select f from FonctionHisto f where f.eai.modifierUsername = ?1 and f.eai.modificationDate <= ?2 and f.eai.modificationDate >= ?3 order by f.eai.modificationDate DESC")
    Page<FonctionHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, PageRequest of);
}