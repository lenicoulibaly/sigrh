package dgmp.sigrh.brokermodule.services;

import dgmp.sigrh.auth.model.events.types.EventType;

public interface IBrokerMessageSender<DTO>
{
    void sendEvent(String topic, EventType eventType, DTO dto);
    void sendEvent(String topic,EventType eventType, DTO dto, boolean withPayload);
}
