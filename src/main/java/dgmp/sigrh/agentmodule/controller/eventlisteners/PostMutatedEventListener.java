package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.structuremodule.controller.repositories.post.PostRepo;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PostMutatedEventListener
{
    private final PostRepo postDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Post>> onPostMutatedEvent()
    {
        return (event)-> postDAO.save(event.getNewlyMutatedObject());
    }
}
