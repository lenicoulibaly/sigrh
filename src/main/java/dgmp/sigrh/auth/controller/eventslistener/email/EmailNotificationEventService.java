package dgmp.sigrh.auth.controller.eventslistener.email;

import dgmp.sigrh.auth.controller.repositories.AccountTokenDAO;
import dgmp.sigrh.auth.controller.validators.Exceptions.AppException;
import dgmp.sigrh.auth.model.dtos.emailNotification.EmailNotification;
import dgmp.sigrh.auth.model.entities.AccountToken;
import dgmp.sigrh.auth.model.events.types.email.EmailEventTypes;
import dgmp.sigrh.brokermodule.model.MutatedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dgmp.sigrh.auth.controller.validators.Exceptions.ErrorMsg.UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG;

@Service
@RequiredArgsConstructor
public class EmailNotificationEventService implements IEmailNotificationEventService
{
    private final AccountTokenDAO tokenDAO;
    @Override
    public void onEmailNotificationSent(MutatedEventPayload<EmailEventTypes, EmailNotification> event)
    {
        AccountToken token = tokenDAO.findByToken(event.getNewlyMutatedObject().getToken()).orElseThrow(()->new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG));
        token.setEmailSent(true);
        tokenDAO.save(token);
    }
}
