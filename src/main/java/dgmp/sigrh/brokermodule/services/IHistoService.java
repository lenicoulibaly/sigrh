package dgmp.sigrh.brokermodule.services;

import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface IHistoService <Entity, HistoEntity, EventType>
{
    HistoEntity storeEntity(Entity entity, EventType eventType);
    Page<HistoEntity> getHistoPageBetweenPeriod(Long entityId, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize);
    Page<HistoEntity> getHistoPageBetweenPeriod(Long entityId, String username, LocalDateTime after, LocalDateTime before, int pageNum, int pageSize);
}
