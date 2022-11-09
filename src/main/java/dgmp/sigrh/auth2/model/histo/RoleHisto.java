package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.RoleEventTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class RoleHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long roleId;
    private String roleCode;
    private String roleName;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private RoleEventTypes eventType;
}
