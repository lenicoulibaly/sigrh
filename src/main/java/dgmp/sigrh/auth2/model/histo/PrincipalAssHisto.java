package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.entities.Ass;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.AssignationEventTypes;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class PrincipalAssHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    protected Long assId;
    @Embedded
    private Ass ass;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "structure_id")
    private Structure structure;
    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private AssignationEventTypes eventType;
}
