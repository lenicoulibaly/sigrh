package dgmp.sigrh.structuremodule.model.entities.structure;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.events.StrEventType;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class StrHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long strId;
    //@Column(unique = true)
    private String strCode;
    private String strName;
    private long strLevel;
    private String strSigle;
    @ManyToOne @JoinColumn(name = "PARENT_ID")
    private Structure strParent;
    @ManyToOne @JoinColumn(name="ID_TYPE_UA")
    private Type strType;

    private String strTel;
    private String strAddress;
    private String situationGeo;
    private String creationActFilePath;

    @ManyToOne
    private Post strRespoPost;

    private String ficheTechPath;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;

    @Enumerated(EnumType.STRING)
    private StrEventType eventType;
    @Embedded
    private EventActorIdentifier eai;

    @Transient
    private List<Structure> hierarchy;
}