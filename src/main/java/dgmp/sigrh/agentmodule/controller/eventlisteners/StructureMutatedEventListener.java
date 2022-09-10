package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class StructureMutatedEventListener
{
    private final StrRepo structureDAO;
    //@Bean
    Consumer<MutatedEventPayload<EventType, Structure>> onStructureMutatedEvent()
    {
        return (event)-> structureDAO.save(event.getNewlyMutatedObject());
    }
}
