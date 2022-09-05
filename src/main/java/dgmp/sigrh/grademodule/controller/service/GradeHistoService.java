package dgmp.sigrh.grademodule.controller.service;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.grademodule.controller.repositories.GradeHistoDAO;
import dgmp.sigrh.grademodule.model.dtos.GrageMapper;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.grademodule.model.entities.GradeHisto;
import dgmp.sigrh.grademodule.model.events.GradeEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class GradeHistoService implements IHistoService<Grade, GradeHisto, GradeEventType>
{
    private final GradeHistoDAO gradeHistoDAO;
    private final GrageMapper grageMapper;
    private final ISecurityContextManager scm;
    @Override
    public GradeHisto storeEntity(Grade grade, GradeEventType eventType)
    {
        return gradeHistoDAO.save(grageMapper.mapToGradeHisto(grade, eventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public Page<GradeHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return gradeHistoDAO.searchPageOfGradeHisto(entityId, after, before, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<GradeHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return gradeHistoDAO.searchPageOfGradeHisto(entityId, username, after, before, PageRequest.of(pageNum, pageSize));
    }
}
