package dgmp.sigrh.typemodule.controller.repositories;

import dgmp.sigrh.typemodule.model.entities.TypeParamHisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TypeParamHistoRepo extends JpaRepository<TypeParamHisto, Long>
{
    @Query("select t from TypeParamHisto t where t.typeParamId = ?1 and t.eai.modifierUsername = ?2 and t.eai.modificationDate <= ?3 and t.eai.modificationDate >= ?4 order by t.eai.modificationDate desc")
    Page<TypeParamHisto> getHistoPageBetweenPeriod(Long typeId, String username, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select t from TypeParamHisto t where t.typeParamId = ?1 and t.eai.modificationDate <= ?2 and t.eai.modificationDate >= ?3 order by t.eai.modificationDate desc")
    Page<TypeParamHisto> getHistoPageBetweenPeriod(Long typeId, LocalDateTime before, LocalDateTime after, Pageable pageable);

    @Query("select t from TypeParamHisto t where t.eai.modifierUsername = ?1 and t.eai.modificationDate <= ?2 and t.eai.modificationDate >= ?3 order by t.eai.modificationDate desc")
    Page<TypeParamHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, Pageable pageable);
}