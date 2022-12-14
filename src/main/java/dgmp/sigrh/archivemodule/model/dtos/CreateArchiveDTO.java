package dgmp.sigrh.archivemodule.model.dtos;

import dgmp.sigrh.agentmodule.model.dtos.ExistingOrNullAgtId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@ValidFileExtension
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateArchiveDTO
{
    private String archiveNum;
    @NotNull(message = "La description de l'archive ne peut être nulle")
    @NotBlank(message = "La description de l'archive ne peut être nulle")
    private String description;
    @ValidArchiveType @NotNull(message = "Le type d'archive ne peut être nul")
    private String archiveTypeCode;
    @ExistingOrNullAgtId(message = "L'ID de l'agent est invalide")
    @NotNull(message = "L'ID de l'agent ne peut être nul") @NotBlank(message = "L'ID de l'agent ne peut être nul")
    private Long agtId;

    @NotNull(message = "Le fichier ne peut être nulle")
    @ValidFileSize
    private MultipartFile file;
}
