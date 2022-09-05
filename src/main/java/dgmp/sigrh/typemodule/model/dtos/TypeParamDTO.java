package dgmp.sigrh.typemodule.model.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TypeParamDTO
{
    @NotNull(message = "L'ID du sous type ne peut être null")
    @ExistingTypeId(message = "Sous type inexistant")
    private Long childId;

    @NotNull(message = "L'ID du type parent ne peut être null")
    @ExistingTypeId(message = "type parent inexistant")
    private Long parentId;
}
