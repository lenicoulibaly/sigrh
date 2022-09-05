package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.dtos.emailNotification.EmailNotification;
import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.auth.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;

public interface IBrokerMessageSender<DTO>
{
    void sendEvent(String topic, EventType eventType, DTO dto);
    void sendEvent(EventType eventType, DTO dto);
    void sendEvent(String topic,EventType eventType, DTO dto, boolean withPayload);
    void sendEvent(EventType eventType, DTO dto, boolean withPayload);
    void sendNotificationEvent(MutatedEventPayload<EmailEventTypes, EmailNotification> event);
}
