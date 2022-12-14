package dgmp.sigrh.agentmodule.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dgmp.sigrh.agentmodule.model.dtos.RegisterAgentDTO;
import dgmp.sigrh.agentmodule.model.enums.*;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.grademodule.model.entities.Grade;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.post.Post;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Agent
{
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agt_id_gen")
	@SequenceGenerator(name="agt_id_gen", sequenceName = "agt_id_seq", allocationSize=1)
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
	
	@Transient
	private LocalDate dateDepartRetraite;
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
	
	
	@Transient private long age;

    public Agent(Long agtId) {
    	this.agentId = agtId;
    }

    public long getAge()
	{
		return Period.between(dateNaissance, LocalDate.now()).getYears();
	}



	@Override
	public String toString()
	{
		return this.nom + " " + this.prenom + " ("+this.matricule+ ")";
	}


	public static Agent getAgent(RegisterAgentDTO dto)
	{
		Agent agent = new Agent();
		if(dto == null) return  null;
		BeanUtils.copyProperties(dto, agent, "civilite", "typePiece", "situationMatrimoniale", "situationPresence", "statutAgent", "position", "etatRecrutement");

		agent.emploi = new Emploi(dto.getIdEmploi());
		agent.grade = new Grade(dto.getIdGrade());

		agent.civilite = EnumUtils.getEnum(Civility.class, dto.getCivilite());
		agent.typePiece = EnumUtils.getEnum(TypePiece.class, dto.getTypePiece());
		agent.situationMatrimoniale = EnumUtils.getEnum(SituationMatrimoniale.class, dto.getSituationMatrimoniale());
		agent.situationPresence = EnumUtils.getEnum(SituationPresence.class, dto.getSituationPresence());
		agent.typeAgent = EnumUtils.getEnum(TypeAgent.class, dto.getTypeAgent());
		agent.etatRecrutement = EnumUtils.getEnum(EtatRecrutement.class, dto.getEtatRecrutement());

		return agent;
	}
}