package dgmp.sigrh.notificationmodule.model.entities;

import dgmp.sigrh.auth2.model.entities.AppUser;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailNotification
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
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

    private String systemMailSender; /* L'adresse mail utilisée pour envoyer le mail*/

    public EmailNotification(AppUser user, String mailObject, String token, Long connectedUserId)
    {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.sendingDate = LocalDateTime.now();
        this.token = token;
        this.mailObject = mailObject;
        this.senderUserId = connectedUserId;
    }
}
