package dgmp.sigrh.auth.controller.eventslistener.email;

import dgmp.sigrh.auth.model.dtos.emailNotification.EmailNotification;
import dgmp.sigrh.auth.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;

public interface IEmailNotificationEventService
{
    void onEmailNotificationSent(MutatedEventPayload<EmailEventTypes, EmailNotification> event);
}
