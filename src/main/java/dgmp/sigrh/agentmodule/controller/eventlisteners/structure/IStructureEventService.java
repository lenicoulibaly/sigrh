package dgmp.sigrh.agentmodule.controller.eventlisteners.structure;

import dgmp.sigrh.auth.model.events.types.auth.StructureEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;

public interface IStructureEventService
{
    void onStructureMutatedEvent(MutatedEventPayload<StructureEventTypes, Structure> event);
}
