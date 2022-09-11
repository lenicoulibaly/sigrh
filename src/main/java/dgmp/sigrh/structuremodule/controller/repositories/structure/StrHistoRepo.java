package dgmp.sigrh.structuremodule.controller.repositories.structure;

import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface StrHistoRepo extends JpaRepository<StrHisto, Long>
{
    @Query("select s from StrHisto s where s.strId = ?1 and s.eai.modificationDate <= ?2 and s.eai.modificationDate >= ?3")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.eai.modifierUsername = ?1 and s.eai.modificationDate <= ?2 and s.eai.modificationDate >= ?3")
    Page<StrHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select s from StrHisto s where s.strId = ?1 and s.eai.modifierUsername = ?2 and s.eai.modificationDate <= ?3 and s.eai.modificationDate >= ?4")
    Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, LocalDateTime before, LocalDateTime after, Pageable pageable);
}