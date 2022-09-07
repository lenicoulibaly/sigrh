package dgmp.sigrh.structuremodule.model.entities;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.model.entities.Type;
import dgmp.sigrh.typemodule.model.events.TypeEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class StructureHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strId;
    @Column(unique = true)
    private String strCode;
    private String strName;
    private long strLevel;
    private String strSigle;
    @ManyToOne @JoinColumn(name = "PARENT_ID")
    private StructureHisto strParent;
    @ManyToOne @JoinColumn(name="ID_TYPE_UA")
    private Type typeStructure;

    private String strTel;
    private String strAddress;
    private String situationGeo;
    private String creationActFilePath;

    @ManyToOne
    private Post strRespoPost;

    public StructureHisto(Long strId)
    {
        this.strId = strId;
    }

    private String ficheTechPath;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;

    @Enumerated(EnumType.STRING)
    private TypeEventType eventType;
    @Embedded
    private EventActorIdentifier eai;

}
