package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class PrivilegeHisto
{
    @Id @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long histoId;
    private Long privilegeId;
    private String privilegeCode;
    private String privilegeName;

    @ManyToOne() @JoinColumn(name = "PRV_TYPE_ID")
    private Type prvType;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private PrivilegeEventTypes eventType;
}
