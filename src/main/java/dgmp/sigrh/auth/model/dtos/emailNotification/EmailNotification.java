package dgmp.sigrh.auth.model.dtos.emailNotification;

import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailNotification
{
    private Long mailId;
    private String username;
    private String email; //Recipient Email
    private String token;
    private LocalDateTime sendingDate;
    private boolean seen;
    private boolean sent;
    private String mailObject;
    private String mailMessage;

    private Long senderUserId;
    private String senderUsername;
    private Long senderAssId;
    private Long senderStrId;
    private String senderStrName;
    private Long senderRoleId;
    private String senderRoleName;
    private Long senderAgentId;

    private String systemMailSender; /* L'adresse mail utilisée pour envoyer le mail*/

    public EmailNotification(String emailObject, AppUser user, EventActorIdentifier eventIdentifier, String token)
    {
        this.setSendingDate(LocalDateTime.now());
        this.token = token;
        this.setEmail(user.getEmail());
        this.setUsername(user.getUsername());
        this.mailObject = emailObject;
        this.senderUserId = eventIdentifier.getModifierUserId();
        this.senderAgentId = eventIdentifier.getAgentId();
        this.senderUsername = eventIdentifier.getModifierUsername();
        this.senderAssId = eventIdentifier.getModifierAssId();
        this.senderStrId = eventIdentifier.getModifierStrId();
        this.senderStrName = eventIdentifier.getModifierStrName();
        this.senderRoleId = eventIdentifier.getModifierRoleId();
        this.senderRoleName = eventIdentifier.getModifierRoleName();
        this.seen = false;
        this.sent = false;
    }
}
