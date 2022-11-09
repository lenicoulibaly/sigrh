package dgmp.sigrh.typemodule.model.entities;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private  Long typeId;
    @Enumerated(EnumType.STRING)
    private TypeGroup typeGroup;
    private String uniqueCode;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
    @Enumerated(EnumType.STRING)
    private TypeEventType eventType;
    @Embedded
    private EventActorIdentifier eai;
}
