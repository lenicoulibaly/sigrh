package dgmp.sigrh.structuremodule.model.entities;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Structure
{
    @Id //@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long strId;
    @Column(unique = true)
    private String strCode;
    @Column(unique = true)
    private String strName;
    private String strSigle;
    @ManyToOne @JoinColumn(name = "PARENT_ID")
    private Structure strParent;
    private long strLevel;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;

    private String strTel;
    private String strAddress;

    @ManyToOne
    private Agent strRespo;
    @Transient
    private MultipartFile creationAct;

    public Structure(Long strId)
    {
        this.strId = strId;
    }
}
