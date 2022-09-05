package dgmp.sigrh.auth.controller.eventslistener.assignation;

import dgmp.sigrh.auth.controller.eventslistener.IHistoEventService;
import dgmp.sigrh.auth.model.entities.Assignation;
import dgmp.sigrh.auth.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.auth.model.histo.AssignationHisto;

public interface IAssignationEventService extends IHistoEventService<AssignationEventTypes, Assignation, AssignationHisto>
{
}
