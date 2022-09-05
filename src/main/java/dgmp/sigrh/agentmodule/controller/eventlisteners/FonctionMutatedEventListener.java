package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionDAO;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class FonctionMutatedEventListener
{
    private final FonctionDAO fonctionDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Fonction>> onFunctionMutatedEvent()
    {
        return (event)-> fonctionDAO.save(event.getNewlyMutatedObject());
    }
}
