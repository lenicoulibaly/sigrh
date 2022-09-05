package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.PostDAO;
import dgmp.sigrh.structuremodule.model.entities.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PostMutatedEventListener
{
    private final PostDAO postDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Post>> onPostMutatedEvent()
    {
        return (event)-> postDAO.save(event.getNewlyMutatedObject());
    }
}
