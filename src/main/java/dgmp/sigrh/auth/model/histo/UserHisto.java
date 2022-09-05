package dgmp.sigrh.auth.model.histo;

import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.auth.UserEventTypes;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserHisto
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long userHistoId;
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private Long strId;
    private Long agentId;
    private Long defaultFunctionId;
    private UserEventTypes eventType;

    private LocalDateTime mutationDate;
    private Long updaterId;
    private String updaterUsername;
    private Long updaterFunctionId;
    private String updaterFunctionCode;
    private String updaterFunctionName;

    public static UserHisto getUserHisto(AppUser user, EventActorIdentifier eventIdentifier, UserEventTypes eventType)
    {
        if(user == null) return null;
        UserHisto userHisto = new UserHisto();
        BeanUtils.copyProperties(user, userHisto);
        userHisto.eventType = eventType;
        if(eventIdentifier == null) return userHisto;

        BeanUtils.copyProperties(eventIdentifier, userHisto);
        userHisto.mutationDate = eventIdentifier.getModificationDate();

        return userHisto;
    }
}
