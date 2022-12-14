package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.validators.*;
import dgmp.sigrh.emploimodule.model.dtos.ExistingEmploiId;
import dgmp.sigrh.grademodule.model.dtos.ExistingGradeId;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Builder @Data //@Entity
public class CreateNewAgentDTO
{
    @Pattern(regexp = "(^\\w{2,30}\\d{0,4}\\s+)*")
    @Length(min = 2, max = 100)
    @NotNull
    private String nom;
    @NotNull(message = "Le prénom ne peut être nul")
    private String prenom;
    @NotNull(message = "La civilité ne peut être nulle")
    private String civilite;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de naissance ne peut être nulle")
    @BirthDate
    private LocalDate dateNaissance;
    private String departementNaissance;
    private String lieuNaissance; //Localité (Plus précis)
    private String situationMatrimoniale;

    @Email(message = "Veuillez saisir une adresse mail correcte")
    @NotNull(message = "L'adresse mail ne peut être nulle")
    @UniqueEmail
    private String email;
    @UniqueTel
    private String tel;
    private String typePiece;

    @UniqueNumPiece
    private String numPiece;
    private String nomPere;
    private String nomMere;

    @UniqueMatricule
    private String matricule;
    @ExistingEmploiId
    private Long idEmploi;
    @ExistingGradeId
    private Long idGrade;
    private String titre;
    private String situationPresence;
    @PriseServiceDate
    private PriseService priseService = new PriseService();
    private String typeAgent; //Fonctionnaire, Contractuel
    private String position; //Activite, Detachement, Disponibilite, Sous les drapeaux
    private String etatRecrutement;
    private boolean attenteAffectation;
    private MultipartFile photoFile;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class PriseService
    {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent()
        private LocalDate datePriseService1;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PastOrPresent()
        private LocalDate datePriseServiceDGMP;
    }
}

