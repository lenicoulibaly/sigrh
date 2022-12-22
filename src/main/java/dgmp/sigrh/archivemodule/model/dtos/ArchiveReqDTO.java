package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.ExistingOrNullAgtId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ValidProductionAndExpirationDate
@UniqueArchiveNum(message = "archiveNum::Numéro de pièce déjà enregistré")
@ValidFileSize(message = "file::Fichier trop volumineux") @ValidFileExtension(message = "file::Type de fichier non pris en charge")
public class ArchiveReqDTO
{
    @NotNull(message = "L'ID de l'archive ne peut être nul", groups = {OnUpdate.class})
    @ExistingArchiveId(groups = {OnUpdate.class})
    @Null(groups = {OnCreate.class}, message = "L'ID de l'archive doit être nul")
    private Long archiveId;
    private String archiveNum;
    @NotNull(message = "La description de l'archive ne peut être nulle")
    @NotBlank(message = "La description de l'archive ne peut être nulle")
    private String description;
    @ValidArchiveType @NotNull(message = "Le type d'archive ne peut être nul")
    private String archiveTypeCode;
    @ExistingOrNullAgtId(message = "L'ID de l'agent est invalide")
    @NotNull(message = "L'ID de l'agent ne peut être nul")
    private Long agtId;
    @PastOrPresent(message = "La date de production ne peut être future")
    private LocalDate productionDate;
    private LocalDate expirationDate;
    @NotNull(message = "Le fichier ne peut être nul")

    private MultipartFile file;
}
