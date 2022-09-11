package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.typemodule.model.dtos.ExistingTypeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@CompatibleTypeAndParentStr
public class ChangeAncrageDTO
{
    @ExistingStrId @NotNull(message = "L'ID de la structure ne peut être nul")
    private Long strId;
    @ExistingTypeId @NotNull(message = "L'ID du type ne peut être nul")
    private Long newTypeId;
    @ExistingStrId @NotNull(message = "L'ID de la structure de tutelle ne peut être nul")
    private Long newParentId;
}
