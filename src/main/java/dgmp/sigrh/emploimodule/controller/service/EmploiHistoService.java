package dgmp.sigrh.emploimodule.controller.service;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiHistoDAO;
import dgmp.sigrh.emploimodule.model.dtos.EmploiMapper;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.emploimodule.model.histo.EmploiHisto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component @RequiredArgsConstructor
public class EmploiHistoService implements IHistoService<Emploi, EmploiHisto, EmploiEventType>
{
    private final EmploiMapper emploiMapper;
    private final EmploiHistoDAO emploiHistoDAO;
    private final ISecurityContextManager scm;
    @Override
    public EmploiHisto storeEntity(Emploi emploi, EmploiEventType emploiEventType)
    {
        return emploiHistoDAO.save(emploiMapper.mapToEmploiHisto(emploi, emploiEventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public Page<EmploiHisto> getHistoPageBetweenPeriod(Long entityId,LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return emploiHistoDAO.getHistoPageBetweenPeriod(entityId,after, before, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<EmploiHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return emploiHistoDAO.getHistoPageBetweenPeriod(entityId, username, after, before, PageRequest.of(pageNum, pageSize));
    }
}
