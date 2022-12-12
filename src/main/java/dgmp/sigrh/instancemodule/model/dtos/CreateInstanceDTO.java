package dgmp.sigrh.instancemodule.model.dtos;

import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@CoherentHeadAndRh
public class CreateInstanceDTO
{
    @NotNull(message = "Le nom de l'instance ne peut être nul")
    @NotBlank(message = "Le nom de l'instance ne peut être nul")
    private String instanceName;
    @ExistingStrId @NotNull(message = "Le chapeau de l'instance ne peut être nul")
    @NoOtherInstanceWithSameHead
    private Long headId;
    @ExistingStrId @NotNull(message = "La structure en charge des ressources humaines de l'instance ne peut être nul")
    @NoOtherInstanceWithSameRH
    private Long rhId;
}