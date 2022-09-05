package dgmp.sigrh.typemodule.model.entities;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "type") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Type {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long typeId;
    @Enumerated(EnumType.STRING)
    private TypeGroup typeGroup;
    @Column(nullable = false, unique = true)
    private String uniqueCode;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
    @Transient
    private List<Type> children;

    public Type(Long typeId) {
        this.typeId = typeId;
    }
}