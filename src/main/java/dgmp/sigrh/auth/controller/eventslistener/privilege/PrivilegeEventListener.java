package dgmp.sigrh.auth.controller.eventslistener.privilege;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor @Slf4j
public class PrivilegeEventListener
{
    private final IPrivilegeEventService userEventService;
    @Bean
    public Consumer<MutatedEventPayload<PrivilegeEventTypes, AppPrivilege>> onPrivilegeMutatedEvent()
    {
        return userEventService::storeEntity;
    }
}
