package dgmp.sigrh.structuremodule.controller.service;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrHistoRepo;
import dgmp.sigrh.structuremodule.model.dtos.str.StrMapper;
import dgmp.sigrh.structuremodule.model.entities.structure.StrHisto;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StrHistoService implements IHistoService<Structure, StrHisto, StrEventType>
{
    private final StrHistoRepo strHistoRepo;
    private final StrMapper strMapper;
    private final ISecurityContextManager scm;
    @Override
    public StrHisto storeEntity(Structure str, StrEventType eventType)
    {
        return strHistoRepo.save(strMapper.mapToStrHisto(str, eventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public Page<StrHisto> getHistoPageBetweenPeriod(Long strId, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize)
    {
        return strHistoRepo.getHistoPageBetweenPeriod(strId, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<StrHisto> getHistoPageBetweenPeriod(Long strId, String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return strHistoRepo.getHistoPageBetweenPeriod(strId, username, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<StrHisto> getHistoPageBetweenPeriod(String username, LocalDateTime before, LocalDateTime after, int pageNum, int pageSize) {
        return strHistoRepo.getHistoPageBetweenPeriod(username, before, after, PageRequest.of(pageNum, pageSize));
    }
}
