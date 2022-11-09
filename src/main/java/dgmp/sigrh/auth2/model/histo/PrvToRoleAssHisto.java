package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.Ass;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.AssignationEventTypes;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class PrvToRoleAssHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long assId;
    @Embedded
    private Ass ass;
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private AssignationEventTypes eventType;
}
