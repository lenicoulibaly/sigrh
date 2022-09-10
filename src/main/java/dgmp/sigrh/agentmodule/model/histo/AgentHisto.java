package dgmp.sigrh.agentmodule.model.histo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.agent.AgentEventTypes;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgentHisto
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long AgentHistoId;
    private Long idAgent;
    @Column(length = 50)
    private String nom;
    @Column(length = 50)
    private String prenom;
    @Column(length = 5)
    private String sexe;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String emailPro;

    @Column(length = 20)
    private String tel;
    private String fixeBureau;
    private String lieuNaissance; //Localité (Plus précis)
    private String departementNaissance;
    private LocalDate dateNaissance;
    private String typePiece;
    @Column(length = 50)
    private String numPiece;
    private String nomPere;
    private String nomMere;
    private boolean attenteAffectation;

    private String numBadge;
    @Column(length = 20)
    private String matricule;
    private String situationMatrimonial;
    private long idUserCreateur;
    private long idUserDerniereModif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereModif;

    @OneToOne
    @JoinColumn(name = "ID_POST")
    @JsonIgnore
    private Post post;
    @ManyToOne @JoinColumn(name = "ID_EMPLOI")
    private Emploi emploi;
    @ManyToOne @JoinColumn(name = "ID_GRADE")
    private Grade grade;
    private String titre;


    private String situationPresence;
    private LocalDate datePriseService1;

    private LocalDate datePriseServiceDGMP;

    private String statutAgent; //Fonctionnaire, Contractuel
    private String position; //Activite, Detachement, Disponibilite, Sous les drapeaux
    private String etatRecrutement; // En service, En attente de première affectation, En attente d'affectation dans une SD, En attente d'affectation dans un service
    private boolean active;
    @OneToOne()
    private AppUser user;
    private String nomPhoto;
    private AgentEventTypes eventType;

    private LocalDateTime mutationDate;
    private Long updaterId;
    private String updaterUsername;
    private Long updaterFunctionId;
    private String updaterFunctionCode;
    private String updaterFunctionName;

    public static AgentHisto getAgentHisto(Agent agent, EventActorIdentifier eventIdentifier, AgentEventTypes eventType)
    {
        if(agent == null) return null;
        AgentHisto agentHisto = new AgentHisto();
        BeanUtils.copyProperties(agent, agentHisto);
        agentHisto.eventType = eventType;
        if(eventIdentifier == null) return agentHisto;

        BeanUtils.copyProperties(eventIdentifier, agentHisto);
        agentHisto.mutationDate = eventIdentifier.getModificationDate();

        return agentHisto;
    }
}
