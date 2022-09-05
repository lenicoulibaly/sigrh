package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.model.enums.Civility;
import dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale;
import dgmp.sigrh.agentmodule.model.enums.TypeAgent;
import dgmp.sigrh.agentmodule.model.enums.TypePiece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Builder @Data
public class UpdateAgentDTO
{
    private Long idAgent;
    private String nom;
    private String prenom;
    private Civility civilite;
    private String email;
    private String emailPro;
    private String tel;
    private String fixeBureau;
    private String lieuNaissance; //Localité (Plus précis)
    private String departementNaissance;
    private LocalDate dateNaissance;
    private TypePiece typePiece;
    private String numPiece;
    private String nomPere;
    private String nomMere;
    private boolean attenteAffectation;
    private String numBadge;
    private String matricule;
    private SituationMatrimoniale situationMatrimoniale;
    private LocalDate datePriseService1;
    private LocalDate datePriseServiceDGMP;
    private TypeAgent typeAgent; //Fonctionnaire, Contractuel

    private MultipartFile photoFile;
}
