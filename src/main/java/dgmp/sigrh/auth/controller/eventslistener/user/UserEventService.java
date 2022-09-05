package dgmp.sigrh.auth.controller.eventslistener.user;

import dgmp.sigrh.auth.controller.repositories.UserHistoDAO;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.auth.model.histo.UserHisto;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventService implements IUserEventService
{
    private final UserHistoDAO userHistoDAO;
    @Override
    public UserHisto storeEntity(MutatedEventPayload<UserEventTypes, AppUser> event) {
        return userHistoDAO.save(UserHisto.getUserHisto(event.getNewlyMutatedObject(), event.getEventIdentifier(), event.getEventType()));
    }
}
