package dgmp.sigrh.typemodule.model.entities;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "type_param") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeParam
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeParamId;
    @ManyToOne @JoinColumn(name = "parent_id")
    private Type parent;
    @ManyToOne @JoinColumn(name = "child_id")
    private Type child;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
}