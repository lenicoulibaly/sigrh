package dgmp.sigrh.auth.controller.eventslistener.email;

import dgmp.sigrh.auth.model.dtos.emailNotification.EmailNotification;
import dgmp.sigrh.auth.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EmailNotificationEventListener
{
    private final IEmailNotificationEventService emailNotificationEventService;
    @Bean
    public Consumer<MutatedEventPayload<EmailEventTypes, EmailNotification>> onEmailNotificationSent()
    {
        return emailNotificationEventService::onEmailNotificationSent;
    }
}
