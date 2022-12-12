package dgmp.sigrh.instancemodule.model.entities;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Instance
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instanceId;
    private String instanceName;
    @OneToOne @JoinColumn(name = "head_id")
    private Structure head;
    @OneToOne @JoinColumn(name = "rh_id")
    private Structure rh;

    public Instance(Long instanceId)
    {
        this.instanceId = instanceId;
    }
}
