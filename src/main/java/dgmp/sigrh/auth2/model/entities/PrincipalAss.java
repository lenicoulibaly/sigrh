package dgmp.sigrh.auth2.model.entities;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class PrincipalAss
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long assId;
    private String intitule;
    @Embedded
    private Ass ass;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "structure_id")
    private Structure structure;

    public PrincipalAss(Long assId) {
        this.assId = assId;
    }
}
