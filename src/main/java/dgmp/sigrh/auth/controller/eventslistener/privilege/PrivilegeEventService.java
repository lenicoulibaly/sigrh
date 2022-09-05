package dgmp.sigrh.auth.controller.eventslistener.privilege;

import dgmp.sigrh.auth.controller.repositories.PrivilegeHistoDAO;
import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.auth.model.histo.PrivilegeHisto;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeEventService implements IPrivilegeEventService
{
    private final PrivilegeHistoDAO privilegeHistoDAO;
    @Override
    public PrivilegeHisto storeEntity(MutatedEventPayload<PrivilegeEventTypes, AppPrivilege> event) {
        return privilegeHistoDAO.save(PrivilegeHisto.getPrivilegeHisto(event.getNewlyMutatedObject(), event.getEventIdentifier(), event.getEventType()));
    }
}
