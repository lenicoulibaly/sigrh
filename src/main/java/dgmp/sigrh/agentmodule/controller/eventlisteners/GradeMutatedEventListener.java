package dgmp.sigrh.agentmodule.controller.eventlisteners;

import dgmp.sigrh.auth.model.events.types.EventType;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import dgmp.sigrh.grademodule.controller.repositories.GradeDAO;
import dgmp.sigrh.grademodule.model.entities.Grade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class GradeMutatedEventListener
{
    private final GradeDAO gradeDAO;
    @Bean
    Consumer<MutatedEventPayload<EventType, Grade>> onGradeMutatedEvent()
    {
        return (event)-> gradeDAO.save(event.getNewlyMutatedObject());
    }
}
