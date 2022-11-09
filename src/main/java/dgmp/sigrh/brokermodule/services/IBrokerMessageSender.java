package dgmp.sigrh.brokermodule.services;


import dgmp.sigrh.shared.model.enums.EventType;

public interface IBrokerMessageSender<DTO>
{
    void sendEvent(String topic, EventType eventType, DTO dto);
    void sendEvent(String topic,EventType eventType, DTO dto, boolean withPayload);
}
