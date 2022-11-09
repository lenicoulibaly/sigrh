package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.shared.model.enums.EventType;

public interface IBrokerMessageSender<DTO>
{
    void sendEvent(String topic, EventType eventType, DTO dto);
    void sendEvent(EventType eventType, DTO dto);
    void sendEvent(String topic,EventType eventType, DTO dto, boolean withPayload);
    void sendEvent(EventType eventType, DTO dto, boolean withPayload);
}
