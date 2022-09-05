package dgmp.sigrh.auth.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "privilege_group") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrivilegeGroup
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(unique = true)
    private String groupCode;
}