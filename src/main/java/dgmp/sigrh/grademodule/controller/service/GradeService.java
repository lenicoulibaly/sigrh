package dgmp.sigrh.grademodule.controller.service;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.grademodule.controller.repositories.GradeDAO;
import dgmp.sigrh.grademodule.model.dtos.CreateGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.GrageMapper;
import dgmp.sigrh.grademodule.model.dtos.ReadGradeDTO;
import dgmp.sigrh.grademodule.model.dtos.UpdateGradeDTO;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.grademodule.model.entities.GradeHisto;
import dgmp.sigrh.grademodule.model.enums.Categorie;
import dgmp.sigrh.grademodule.model.events.GradeEventType;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("gradeService") //@Profile("prod")
@RequiredArgsConstructor
public class GradeService implements IGradeService
{
    private final GradeDAO gradeDAO;
    private final IHistoService<Grade, GradeHisto, GradeEventType> gradeHistoService;
    private final GrageMapper grageMapper;
    @Override
    public List<ReadGradeDTO> getAllGrades() {
        return gradeDAO.findActiveStatus().stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
    }

    @Override
    public ReadGradeDTO getGradesById(Long grdId)
    {
        Grade grade = gradeDAO.findById(grdId).orElse(null);
        return grade == null ? null : grageMapper.mapToReadGradeDTO(grade);
    }

    @Override
    public List<ReadGradeDTO> getGradesByCategoryName(String catName)
    {
        return gradeDAO.findByCategorieAndStatus(catName).stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
    }

    //========================================================================================

    @Override
    public Page<ReadGradeDTO> searchPageOfGrades(String key, int pageNum, int pageSize)
    {
        Page<Grade> grades = gradeDAO.searchPageOfGrades(key, PageRequest.of(pageNum, pageSize));
        List<ReadGradeDTO> gradeDTOS = grades.stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
        return new PageImpl<>(gradeDTOS, PageRequest.of(pageNum, pageSize), gradeDAO.countByGradeNameKey(key));
    }

    @Override
    public Page<ReadGradeDTO> searchPageOfDeletedGrades(String key, int pageNum, int pageSize)
    {
        Page<Grade> grades = gradeDAO.searchPageOfDeletedGrades(key, PageRequest.of(pageNum, pageSize));
        List<ReadGradeDTO> gradeDTOS = grades.stream().map(grageMapper::mapToReadGradeDTO).collect(Collectors.toList());
        return new PageImpl<>(gradeDTOS, PageRequest.of(pageNum, pageSize), gradeDAO.countDeletedGrades());
    }

    @Override @Transactional
    public ReadGradeDTO createGrade(CreateGradeDTO dto)
    {
        Grade grade = gradeDAO.save(grageMapper.mapToGrade(dto));
        gradeHistoService.storeEntity(grade, GradeEventType.CREATE_GRADE);
        return grageMapper.mapToReadGradeDTO(grade);
    }

    @Override @Transactional
    public ReadGradeDTO updateGrade(UpdateGradeDTO dto)
    {
        Grade grade = gradeDAO.findById(dto.getIdGrade()).orElse(null);
        if(grade==null) return null;
        grade.setNomGrade(dto.getCategorie() + dto.getRang());
        grade.setCategorie(EnumUtils.getEnum(Categorie.class, dto.getCategorie()));
        grade.setRang(dto.getRang());
        gradeHistoService.storeEntity(grade, GradeEventType.UPDATE_GRADE);
        return grageMapper.mapToReadGradeDTO(grade);
    }

    @Override @Transactional
    public void deleteGrade(Long idGrade)
    {
        Grade grade = gradeDAO.findById(idGrade).orElse(null);
        if(grade==null) return ;
        if(grade.getStatus() == PersistenceStatus.DELETED) return;
        grade.setStatus(PersistenceStatus.DELETED);
        gradeHistoService.storeEntity(grade, GradeEventType.DELETE_GRADE);
    }

    @Override @Transactional
    public void restoreGrade(Long idGrade)
    {
        Grade grade = gradeDAO.findById(idGrade).orElse(null);
        if(grade==null) return ;
        if(grade.getStatus() == PersistenceStatus.ACTIVE) return;
        grade.setStatus(PersistenceStatus.ACTIVE);
        gradeHistoService.storeEntity(grade, GradeEventType.RESTORE_GRADE);
    }
}
