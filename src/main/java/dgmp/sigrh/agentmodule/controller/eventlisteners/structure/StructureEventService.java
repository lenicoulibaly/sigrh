package dgmp.sigrh.agentmodule.controller.eventlisteners.structure;

import dgmp.sigrh.auth.model.events.types.auth.StructureEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StructureEventService implements IStructureEventService
{
    private final StrRepo strDAO;
    @Override
    public void onStructureMutatedEvent(MutatedEventPayload<StructureEventTypes, Structure> event)
    {
        strDAO.save(event.getNewlyMutatedObject());
    }
}
