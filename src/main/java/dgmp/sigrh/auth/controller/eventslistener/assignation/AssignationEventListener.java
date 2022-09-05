package dgmp.sigrh.auth.controller.eventslistener.assignation;

import dgmp.sigrh.auth.model.entities.Assignation;
import dgmp.sigrh.auth.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor @Slf4j
public class AssignationEventListener
{
    private final IAssignationEventService assignationEventService;
    @Bean
    public Consumer<MutatedEventPayload<AssignationEventTypes, Assignation>> onAssignationMutatedEvent()
    {
        return assignationEventService::storeEntity;
    }
}
