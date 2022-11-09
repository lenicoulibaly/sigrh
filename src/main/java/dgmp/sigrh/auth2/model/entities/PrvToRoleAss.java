package dgmp.sigrh.auth2.model.entities;

import lombok.*;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRIVILEGE_TO_ROLE")
public class PrvToRoleAss
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assId;
    @Embedded
    private Ass ass;
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
}
