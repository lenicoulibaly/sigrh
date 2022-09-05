package dgmp.sigrh.auth.controller.eventslistener.user;

import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor @Slf4j
public class UserEventListener
{
    private final IUserEventService userEventService;
    @Bean
    public Consumer<MutatedEventPayload<UserEventTypes, AppUser>> onUserMutatedEvent()
    {
        return userEventService::storeEntity;
    }
}
