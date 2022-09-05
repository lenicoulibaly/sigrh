package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.StructureDAO;
import dgmp.sigrh.structuremodule.model.entities.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class StructureMutatedEventListener
{
    private final StructureDAO structureDAO;
    //@Bean
    Consumer<MutatedEventPayload<EventType, Structure>> onStructureMutatedEvent()
    {
        return (event)-> structureDAO.save(event.getNewlyMutatedObject());
    }
}
