package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.enums.PrvGroup;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.PrivilegeEventTypes;
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

    @Enumerated(EnumType.STRING)
    private PrvGroup prvGroup;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private PrivilegeEventTypes eventType;
}
