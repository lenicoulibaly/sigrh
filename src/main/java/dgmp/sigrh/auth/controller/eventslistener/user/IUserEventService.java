package dgmp.sigrh.auth.controller.eventslistener.user;

import dgmp.sigrh.auth.controller.eventslistener.IHistoEventService;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.auth.model.histo.UserHisto;

public interface IUserEventService extends IHistoEventService<UserEventTypes, AppUser, UserHisto>
{
}
