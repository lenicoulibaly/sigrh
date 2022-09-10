package dgmp.sigrh.structuremodule.model.entities.structure;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Structure
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strId;
    @Column(unique = true)
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
    @Transient
    private List<Structure> strChildren;
    @Transient
    private MultipartFile creationActFile;

    public Structure(Long strId)
    {
        this.strId = strId;
    }

    private String ficheTechPath;
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;

    @Transient
    private List<Post> posts;
    @Transient
    private List<Agent> personnel;

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }

}
