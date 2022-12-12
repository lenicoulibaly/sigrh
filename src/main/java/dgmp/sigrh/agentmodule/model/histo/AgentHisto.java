package dgmp.sigrh.agentmodule.model.histo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dgmp.sigrh.agentmodule.model.enums.*;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.agent.AgentEventTypes;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgentHisto
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agt_histo_id_gen")
    @SequenceGenerator(name="agt_histo_id_gen", sequenceName = "agt_histo_id_seq", allocationSize=1)
    private Long histoId;
    private Long agentId;
    @Column(length = 50)
    private String nom;
    @Column(length = 50)
    private String prenom;
    @Column(length = 30) @Enumerated(EnumType.STRING)
    private Civility civilite;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100, unique = true)
    private String emailPro;

    @Column(length = 20, unique = true)
    private String tel;
    private String fixeBureau;
    private String lieuNaissance; //Localité (Plus précis)
    private String departementNaissance;
    private LocalDate dateNaissance;
    @Enumerated(EnumType.STRING)
    private TypePiece typePiece;
    @Column(length = 50, unique = true)
    private String numPiece;
    private String nomPere;
    private String nomMere;
    private boolean attenteAffectation;


    @Column(unique = true)
    private String numBadge;
    @Column(length = 20, unique = true)
    private String matricule;
    @Enumerated(EnumType.STRING)
    private SituationMatrimoniale situationMatrimoniale;

    @OneToOne @JoinColumn(name = "ID_POST")
    @JsonIgnore
    private Post post;
    @OneToOne @JoinColumn(name = "ID_FONCTION")
    @JsonIgnore
    private Fonction fonction;
    @OneToOne @JoinColumn(name = "STR_ID")
    @JsonIgnore
    private Structure structure;
    @ManyToOne @JoinColumn(name = "ID_EMPLOI")
    private Emploi emploi;
    @ManyToOne @JoinColumn(name = "ID_GRADE")
    private Grade grade;
    private String titre;

    @Enumerated(EnumType.STRING)
    private SituationPresence situationPresence;
    private LocalDate datePriseService1;

    private LocalDate datePriseServiceDGMP;

    @Enumerated(EnumType.STRING)
    private TypeAgent typeAgent; //Fonctionnaire, Contractuel
    //@Enumerated(EnumType.STRING)
    //private Position position; //Activite, Detachement, Disponibilite, Sous les drapeaux
    @Enumerated(EnumType.STRING)
    private EtatRecrutement etatRecrutement; // En service, En attente de première affectation, En attente d'affectation dans une SD, En attente d'affectation dans un service
    @Enumerated(EnumType.STRING)
    private PersistenceStatus status;
    @OneToOne(fetch = FetchType.EAGER)
    private AppUser user;
    @Column(unique = true)
    private String nomPhoto;
    @Transient private MultipartFile photoFile;

    @Enumerated(EnumType.STRING)
    private AgentEventTypes eventType;
    @Embedded
    private EventActorIdentifier eai;
}
