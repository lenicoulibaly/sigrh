package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.grademodule.controller.repositories.CategoryDAO;
import dgmp.sigrh.grademodule.model.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CategoryMutatedEventListener
{
    private final CategoryDAO categoryDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Category>> onCategoryMutatedEvent()
    {
        return (event)-> categoryDAO.save(event.getNewlyMutatedObject());
    }
}
