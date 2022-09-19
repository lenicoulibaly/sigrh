package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.typemodule.controller.repositories.TypeParamHistoRepo;
import dgmp.sigrh.typemodule.model.dtos.TypeMapper;
import dgmp.sigrh.typemodule.model.entities.TypeParam;
import dgmp.sigrh.typemodule.model.entities.TypeParamHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TypeParamHistoService implements IHistoService<TypeParam, TypeParamHisto, TypeEventType>
{
    private final TypeParamHistoRepo typeParamHistoRepo;
    private final ISecurityContextManager scm;
    private final TypeMapper typeMapper;
    @Override
    public TypeParamHisto storeEntity(TypeParam typeParam, TypeEventType eventType)
    {
        return typeParamHistoRepo.save(typeMapper.mapToTypeParamHisto(typeParam, eventType, scm.getEventActorIdFromSCM()));
    }

    @Override
    public TypeParamHisto storeEntity(TypeParam typeParam, TypeEventType eventType, String actionId, String mainActionName)
    {
        return typeParamHistoRepo.save(typeMapper.mapToTypeParamHisto(typeParam, eventType, scm.getEventActorIdFromSCM(), actionId, mainActionName));
    }

    @Override
    public Page<TypeParamHisto> getHistoPageBetweenPeriod(Long typeId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return typeParamHistoRepo.getHistoPageBetweenPeriod(typeId, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<TypeParamHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return typeParamHistoRepo.getHistoPageBetweenPeriod(entityId, username, before, after, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<TypeParamHisto> getHistoPageBetweenPeriod(String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return typeParamHistoRepo.getHistoPageBetweenPeriod(username, before, after, PageRequest.of(pageNum, pageSize));
    }
}
