package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.Ass;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.AssignationEventTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class RoleAssHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long assId;
    @Embedded
    private Ass ass;
    @ManyToOne() @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    @ManyToOne() @JoinColumn(name = "PRINCIPAL_ASS_ID")
    private PrincipalAss principalAss;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private AssignationEventTypes eventType;
}
