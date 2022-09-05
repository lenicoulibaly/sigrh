package dgmp.sigrh.auth.controller.eventslistener.assignation;

import dgmp.sigrh.auth.controller.repositories.AssignationHistoDAO;
import dgmp.sigrh.auth.model.entities.Assignation;
import dgmp.sigrh.auth.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.auth.model.histo.AssignationHisto;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignationEventService implements IAssignationEventService
{
    private final AssignationHistoDAO assignationHistoDAO;
    @Override
    public AssignationHisto storeEntity(MutatedEventPayload<AssignationEventTypes, Assignation> event) {
        return assignationHistoDAO.save(AssignationHisto.getAssignationHisto(event.getNewlyMutatedObject(), event.getEventIdentifier(), event.getEventType()));
    }
}
