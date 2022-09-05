package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EmploiMutatedEventListener
{
    private final EmploiDAO emploiDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Emploi>> onEmploiMutatedEvent()
    {
        return (event)-> emploiDAO.save(event.getNewlyMutatedObject());
    }
}
