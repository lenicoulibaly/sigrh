package dgmp.sigrh.agentmodule.controller.eventlisteners.structure;

import dgmp.sigrh.auth.model.events.types.auth.StructureEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor @Slf4j
public class StructureEventListener
{
    private final IStructureEventService structureEventService;
    @Bean
    public Consumer<MutatedEventPayload<StructureEventTypes, Structure>> onStructureMutatedEvent()
    {
        return structureEventService::onStructureMutatedEvent;
    }
}
