package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.typemodule.model.entities.TypeParam;
import dgmp.sigrh.typemodule.model.entities.TypeParamHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class TypeParamHistoService implements IHistoService<TypeParam, TypeParamHisto, TypeEventType>
{
    @Override
    public TypeParamHisto storeEntity(TypeParam typeParam, TypeEventType eventType) {
        return null;
    }

    @Override
    public Page<TypeParamHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<TypeParamHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return null;
    }
}
