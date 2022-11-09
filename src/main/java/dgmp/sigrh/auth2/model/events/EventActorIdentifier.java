package dgmp.sigrh.auth2.model.events;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class EventActorIdentifier
{
    private String actionId;
    private String mainActionName;
    private LocalDateTime modificationDate;
    private Long modifierUserId;
    private String modifierUsername;
    private Long modifierAssId;
    private Long modifierStrId;
    private String modifierStrName;
    private Long agentId;
    private String nom;
    private String prenom;
    private String matricule;
}
