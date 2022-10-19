package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.typemodule.model.dtos.ExistingTypeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@CompatibleTypeAndParentStr
public class ChangeAnchorDTO
{
    @ExistingStrId @NotNull(message = "L'ID de la structure ne peut être nul")
    private Long strId;
    @ExistingTypeId @NotNull(message = "L'ID du type ne peut être nul")
    private Long newTypeId;
    @ExistingStrId @NotNull(message = "L'ID de la structure de tutelle ne peut être nul")
    private Long newParentId;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ChangeAnchorDTO)) return false;
        ChangeAnchorDTO that = (ChangeAnchorDTO) o;
        return Objects.equals(strId, that.strId) && Objects.equals(newTypeId, that.newTypeId) && Objects.equals(newParentId, that.newParentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strId, newTypeId, newParentId);
    }
}
