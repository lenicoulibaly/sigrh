package dgmp.sigrh.auth.controller.eventslistener;


import dgmp.sigrh.brokermodule.model.MutatedEventPayload;

public interface IHistoEventService<EventType, DataType, HistoType>
{
    HistoType storeEntity(MutatedEventPayload<EventType, DataType> event);
}
