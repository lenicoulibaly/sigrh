package dgmp.sigrh.typemodule.model.dtos;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CreateTypeDTO
{
    @NotBlank(message = "Le groupCode ne peut être null")
    @NotNull(message = "Le groupCode ne peut être null")
    private String typeGroup;

    @NotBlank(message = "Le uniqueCode ne peut être null")
    @NotNull(message = "Le uniqueCode ne peut être null")
    @UniqueTypeCode(message="Ce code est déjà utilisé")
    private String uniqueCode;

    @Length(message = "Le nom du type doit contenir au moins deux caratères", min = 2)
    @NotBlank(message = "Le nom du type ne peut être nul")
    @NotNull(message = "Le nom du type ne peut être nul")
    private String name;
}