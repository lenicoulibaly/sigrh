package dgmp.sigrh.fonctionmodule.controller.service;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionHistoDAO;
import dgmp.sigrh.fonctionmodule.model.dtos.FonctionMapper;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.fonctionmodule.model.events.FonctionEventType;
import dgmp.sigrh.fonctionmodule.model.histo.FonctionHisto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class FonctionHistoService implements IHistoService<Fonction, FonctionHisto, FonctionEventType>
{
    private final FonctionMapper fonctionMapper;
    private final FonctionHistoDAO fonctionHistoDAO;
    private final ISecurityContextManager scm;

    @Override
    public FonctionHisto storeEntity(Fonction fonction, FonctionEventType eventType) {
        return fonctionHistoDAO.save(fonctionMapper.mapToFonctionHisto(fonction, eventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return fonctionHistoDAO.getHistoPageBetweenPeriod(idFonction, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<FonctionHisto> getHistoPageBetweenPeriod(Long idFonction, String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return fonctionHistoDAO.getHistoPageBetweenPeriod(idFonction, username, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<FonctionHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return fonctionHistoDAO.getHistoPageBetweenPeriod(username, before, after, PageRequest.of(pageNum, pageSize));
    }
}
