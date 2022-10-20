package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StrHistoRepo extends JpaRepository<StrHisto, Long>
{
    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modificationDate <= coalesce(?2, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?3, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.eai.modifierUsername = coalesce(?1, s.eai.modifierUsername) and s.eai.modificationDate <= coalesce(?2, s.eai.modificationDate) and s.eai.modificationDate >=  coalesce(?3, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modifierUsername = coalesce(?2, s.eai.modifierUsername) and s.eai.modificationDate <= coalesce(?3, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?4, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modifierUsername = coalesce(?2, s.eai.modifierUsername) and s.eventType = coalesce(?3, s.eventType) and s.eai.modificationDate <= coalesce(?4, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?5, s.eai.modificationDate) ")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, StrEventType eventType, LocalDateTime before, LocalDateTime after, Pageable pageable);
}