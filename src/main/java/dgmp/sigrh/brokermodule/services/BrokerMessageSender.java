package dgmp.sigrh.brokermodule.services;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.auth.security.services.ISecurityContextManager;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor @Slf4j
public class BrokerMessageSender implements IBrokerMessageSender<Agent>
{
    private final StreamBridge streamBridge;
    private final ISecurityContextManager scm;

    @Override
    public void sendEvent(String topic, EventType eventType, Agent agent)
    {
        streamBridge.send(topic, new MutatedEventPayload<>(eventType, agent, scm.getEventActorIdFromSCM()));
    }

    @Override
    public void sendEvent(String topic, EventType eventType, Agent agent, boolean withPayload)
    {
        streamBridge.send(topic, new MutatedEventPayload(eventType, agent, withPayload ? scm.getEventActorIdFromSCM() : null));
    }
}
