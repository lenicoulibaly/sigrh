package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.validators.*;
import dgmp.sigrh.agentmodule.model.enums.Civility;
import dgmp.sigrh.agentmodule.model.enums.SituationMatrimoniale;
import dgmp.sigrh.agentmodule.model.enums.TypeAgent;
import dgmp.sigrh.agentmodule.model.enums.TypePiece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Builder @Data
@UniqueEmail(message = "email::Cette adresse mail est déjà attribuée")
@UniqueEmailPro(message = "emailPro::Cette adresse mail est déjà attribuée")
@UniqueTel(message = "tel::Ce numéro de téléphone est déjà attribué")
@UniqueNumBadge(message = "numBadge::Ce numéro de badge est déjà attribué")
@UniqueNumPiece(message = "numPiece::Ce numéro de pièce est déjà attribué")
@UniqueMatricule(message = "matricule::Ce N° matricule est déjà attribué")
public class UpdateAgentDTO
{
    private Long agentId;
    private String nom;
    private String prenom;
    private Civility civilite;
    @Email(message = "Veuillez saisir une adresse mail correcte")
    @NotNull(message = "L'adresse mail ne peut être nulle")
    @NotBlank(message = "L'adresse mail ne peut être nulle")
    private String email;
    @Email(message = "Veuillez saisir une adresse mail correcte")
    private String emailPro;

    @NotNull(message = "Le N° de téléphone ne peut être nul")
    private String tel;
    private String fixeBureau;
    private String lieuNaissance; //Localité (Plus précis)
    private String departementNaissance;
    private LocalDate dateNaissance;
    private TypePiece typePiece;
    @NotNull(message = "Le N° de pièce ne peut être nul")
    @NotBlank(message = "Le N° de pièce ne peut être nul")
    private String numPiece;
    private String nomPere;
    private String nomMere;

    private String numBadge;
    private String matricule;
    private SituationMatrimoniale situationMatrimoniale;
    private LocalDate datePriseService1;
    private LocalDate datePriseServiceDGMP;
    private TypeAgent typeAgent; //Fonctionnaire, Contractuel

    private MultipartFile photoFile;
}
