package dgmp.sigrh.auth.controller.services.impl.withbroker;

import dgmp.sigrh.SpringContext;
import dgmp.sigrh.auth.controller.services.spec.IBrokerMessageSender;
import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static dgmp.sigrh.auth.security.SecurityConstants.EMAIL_NOTIFICATION_TOPIC;
import static dgmp.sigrh.auth.security.SecurityConstants.USER_TOPIC;

@Component
@RequiredArgsConstructor @Slf4j
@Profile("withbroker")
public class BrokerMessageSender implements IBrokerMessageSender
{
    private final StreamBridge streamBridge;
    //private ISecurityContextManager scm = SpringContext.getBean(ISecurityContextManager.class);

    @Override
    public void sendEvent(String topic, EventType eventType, Object DTO)
    {
        ISecurityContextManager scm = SpringContext.getBean(ISecurityContextManager.class);
        streamBridge.send(topic, new MutatedEventPayload<>(eventType, DTO, scm.getEventActorIdFromSCM()));
    }

    @Override
    public void sendEvent(String topic, EventType eventType, Object DTO, boolean withIdentifier)
    {
        ISecurityContextManager scm = SpringContext.getBean(ISecurityContextManager.class);
        streamBridge.send(topic, new MutatedEventPayload<>(eventType, DTO, withIdentifier ? scm.getEventActorIdFromSCM() : null));
    }

    @Override
    public void sendNotificationEvent(MutatedEventPayload event)
    {
        streamBridge.send(EMAIL_NOTIFICATION_TOPIC, event);
    }

    @Override
    public void sendEvent(EventType eventType, Object DTO, boolean withIdentifier)
    {
        ISecurityContextManager scm = SpringContext.getBean(ISecurityContextManager.class);
        streamBridge.send(USER_TOPIC, MutatedEventPayload.builder()
                .eventType(eventType)
                .newlyMutatedObject(DTO)
                .eventIdentifier(withIdentifier ? scm.getEventActorIdFromSCM(): null)
                .build());
    }

    @Override
    public void sendEvent(EventType eventType, Object DTO)
    {
        ISecurityContextManager scm = SpringContext.getBean(ISecurityContextManager.class);
        streamBridge.send(USER_TOPIC, MutatedEventPayload.builder()
                .eventType(eventType)
                .newlyMutatedObject(DTO)
                .eventIdentifier(scm.getEventActorIdFromSCM())
                .build());
    }
}
