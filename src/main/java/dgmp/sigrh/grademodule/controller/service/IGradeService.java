package dgmp.sigrh.grademodule.controller.service;

import dgmp.sigrh.grademodule.model.dtos.CreateGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.ReadGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.UpdateGradeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IGradeService
{
    List<ReadGradeDTO> getAllGrades();
    ReadGradeDTO getGradesById(Long grdId);
    List<ReadGradeDTO> getGradesByCategoryName(String catName);

    Page<ReadGradeDTO> searchPageOfGrades(String key, int pageNum, int pageSize);
    Page<ReadGradeDTO> searchPageOfDeletedGrades(String key, int pageNum, int pageSize);

    ReadGradeDTO createGrade(CreateGradeDTO dto);
    ReadGradeDTO updateGrade(UpdateGradeDTO dto);
    void deleteGrade(Long idGrade);
    void restoreGrade(Long idGrade);
}
