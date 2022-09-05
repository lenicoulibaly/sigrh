package dgmp.sigrh.grademodule.model.entities;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.grademodule.model.enums.Categorie;
import dgmp.sigrh.grademodule.model.events.GradeEventType;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GradeHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histId;
    private Long idGrade;
    private String nomGrade;
    private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
    @Enumerated(EnumType.STRING)
    private Categorie categorie;
    @Enumerated(EnumType.STRING)
    private GradeEventType eventType;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
    @Embedded
    private EventActorIdentifier eai;
}
