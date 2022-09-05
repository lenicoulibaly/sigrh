package dgmp.sigrh.grademodule.controller.repositories;

import dgmp.sigrh.grademodule.model.entities.GradeHisto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface GradeHistoDAO extends JpaRepository<GradeHisto, Long>
{
    @Query("select g from GradeHisto g where g.idGrade = ?1 and g.eai.modifierUsername = ?2 and g.eai.modificationDate >= ?3 and g.eai.modificationDate <= ?4 order by g.eai.modificationDate DESC")
    Page<GradeHisto> searchPageOfGradeHisto(Long idGrade, String modifierUsername, LocalDateTime after, LocalDateTime before, Pageable pageable);

    @Query("select g from GradeHisto g where g.idGrade = ?1 and g.eai.modificationDate >= ?2 and g.eai.modificationDate <= ?3 order by g.eai.modificationDate DESC")
    Page<GradeHisto> searchPageOfGradeHisto(Long idGrade, LocalDateTime after, LocalDateTime before, Pageable pageable);
}