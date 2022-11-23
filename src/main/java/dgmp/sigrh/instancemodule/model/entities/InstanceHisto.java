package dgmp.sigrh.instancemodule.model.entities;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.instancemodule.model.events.InstanceEventType;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InstanceHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long instanceId;
    private String name;
    @OneToOne @JoinColumn(name = "head_id")
    private Structure head;
    @OneToOne @JoinColumn(name = "rh_id")
    private Structure rh;

    @Enumerated(EnumType.STRING)
    private InstanceEventType eventType;
    @Embedded
    private EventActorIdentifier eai;
}
