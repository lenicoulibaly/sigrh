package dgmp.sigrh.typemodule.controller.services;

import dgmp.sigrh.brokermodule.services.IHistoService;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.entities.TypeHisto;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class TypeHistoService implements IHistoService<Type, TypeHisto, TypeEventType>
{
    @Override
    public TypeHisto storeEntity(Type type, TypeEventType typeEventType) {
        return null;
    }

    @Override
    public Page<TypeHisto> getHistoPageBetweenPeriod(Long entityId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public Page<TypeHisto> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize) {
        return null;
    }
}
