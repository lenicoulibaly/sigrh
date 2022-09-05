package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.typemodule.controller.repositories.TypeHistoRepo;
import dgmp.sigrh.typemodule.model.dtos.TypeMapper;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.entities.TypeHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class TypeHistoService implements IHistoService<Type, TypeHisto, TypeEventType>
{
    private final TypeHistoRepo typeHistoRepo;
    private final ISecurityContextManager scm;
    private final TypeMapper typeMapper;
    @Override
    public TypeHisto storeEntity(Type type, TypeEventType eventType)
    {
        return typeHistoRepo.save(typeMapper.mapToTypeHisto(type, eventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public Page<TypeHisto> getHistoPageBetweenPeriod(Long typeId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize)
    {
        return typeHistoRepo.getHistoPageBetweenPeriod(typeId, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<TypeHisto> getHistoPageBetweenPeriod(Long typeId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return typeHistoRepo.getHistoPageBetweenPeriod(typeId, username, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<TypeHisto> getHistoPageBetweenPeriod(String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize)
    {
        return typeHistoRepo.getHistoPageBetweenPeriod(username, before, after, PageRequest.of(pageNum, pageSize));
    }
}
