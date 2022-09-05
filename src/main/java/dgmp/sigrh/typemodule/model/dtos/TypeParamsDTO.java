package dgmp.sigrh.typemodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TypeParamsDTO
{
    private Long[] childIds;

    @NotNull(message = "L'ID du type parent ne peut être null")
    @ExistingTypeId(message = "type parent inexistant")
    private Long parentId;

    private String status;
}
