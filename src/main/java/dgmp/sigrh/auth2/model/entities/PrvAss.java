package dgmp.sigrh.auth2.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class PrvAss
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assId;
    @Embedded
    private Ass ass;
    @ManyToOne() @JoinColumn(name = "PRV_ID")
    private AppPrivilege prv;
    @ManyToOne() @JoinColumn(name = "PRINCIPAL_ASS_ID")
    private PrincipalAss principalAss;
}
