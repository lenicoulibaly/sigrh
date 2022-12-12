package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.auth2.model.dtos.appuser.ReadUserDTO;
import dgmp.sigrh.emploimodule.model.dtos.ReadEmploiDTO;
import dgmp.sigrh.grademodule.model.dtos.ReadGradeDTO;
import dgmp.sigrh.structuremodule.model.dtos.post.ReadPostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class ReadAgentDTO
{
    private Long agentId                  ;
    private String nom;
    private String prenom;
    private String civilite;
    private String email;
    private String emailPro;
    private String tel;
    private String fixeBureau;
    private String lieuNaissance; //Localité (Plus précis)
    private String departementNaissance;
    private LocalDate dateNaissance;
    private String typePiece;
    private String numPiece;
    private String nomPere;
    private String nomMere;
    private boolean attenteAffectation;
    private String numBadge;
    private String matricule;
    private String situationMatrimoniale;
    private ReadPostDTO readPostDTO;
    private ReadEmploiDTO readEmploiDTO;
    private ReadGradeDTO readGradeDTO;
    private String fonction;
    private String emploi;
    private String grade;
    private String titre;
    private String situationPresence;
    private LocalDate datePriseService1;
    private LocalDate datePriseServiceDGMP;

    private LocalDate dateDepartRetraite;
    private String typeAgent; //Fonctionnaire, Contractuel
    private String position; //Activite, Detachement, Disponibilite, Sous les drapeaux
    private String etatRecrutement; // En service, En attente de première affectation, En attente d'affectation dans une SD, En attente d'affectation dans un service

    private ReadUserDTO readUserDTO;
    private String nomPhoto;
    private MultipartFile photoFile;

    private String strName;
    private String strSigle;
    private String hierarchySigles;
}
