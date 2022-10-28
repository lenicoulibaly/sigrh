package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StrHistoRepo extends JpaRepository<StrHisto, Long>
{
    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modificationDate <= coalesce(?2, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?3, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.eai.modifierUsername = coalesce(?1, s.eai.modifierUsername) and s.eai.modificationDate <= coalesce(?2, s.eai.modificationDate) and s.eai.modificationDate >=  coalesce(?3, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modifierUsername = coalesce(?2, s.eai.modifierUsername) and s.eai.modificationDate <= coalesce(?3, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?4, s.eai.modificationDate)")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.strId = coalesce(?1, s.strId) and s.eai.modifierUsername = coalesce(?2, s.eai.modifierUsername) and s.eventType in ?3 and s.eai.modificationDate <= coalesce(?4, s.eai.modificationDate) and s.eai.modificationDate >= coalesce(?5, s.eai.modificationDate) order by s.eai.modificationDate desc")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, List<StrEventType> eventType, LocalDate before, LocalDate after, Pageable pageable);

    @Query("select h from StrHisto h where h.histoId = function('get_str_histo_id', ?1, ?2)")
    StrHisto getStrHistoByDate(Long strId, LocalDateTime dateTime);

    @Query("select h from StrHisto h where locate(concat(h.strCode, '/'), function('get_str_histo_code', ?1, ?2)) = 1 and " +
            "h.eai.modificationDate in (select max(h2.eai.modificationDate) from StrHisto h2 where " +
            "locate(concat(h2.strCode, '/'), function('get_str_histo_code', ?1, ?2)) = 1 and " +
            "h2.eai.modificationDate <= ?2 group by h2.strLevel) order by h.strLevel asc")
    List<StrHisto> getStrHistoParents(Long strId, LocalDateTime dateTime);

    @Query("select h.strSigle from StrHisto h where locate(concat(h.strCode, '/'), function('get_str_histo_code', ?1, ?2)) = 1 and " +
            "h.eai.modificationDate in (select max(h2.eai.modificationDate) from StrHisto h2 where " +
            "locate(concat(h2.strCode, '/'), function('get_str_histo_code', ?1, ?2)) = 1 and " +
            "h2.eai.modificationDate <= ?2 group by h2.strLevel) order by h.strLevel asc")
    List<String> getStrHistoParentSigles(Long strId, LocalDateTime dateTime);
}