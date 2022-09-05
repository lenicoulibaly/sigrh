package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.brokermodule.model.MutatedEventPayload;

public interface IHistoEventService<EventType, DataType, HistoType>
{
    HistoType storeEntity(MutatedEventPayload<EventType, DataType> event);
}
