package dgmp.sigrh.auth.controller.eventslistener.privilege;

import dgmp.sigrh.auth.controller.eventslistener.IHistoEventService;
import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.auth.model.histo.PrivilegeHisto;

public interface IPrivilegeEventService extends IHistoEventService<PrivilegeEventTypes, AppPrivilege, PrivilegeHisto>
{
}
