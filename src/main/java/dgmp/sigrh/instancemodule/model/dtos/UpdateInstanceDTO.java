package dgmp.sigrh.instancemodule.model.dtos;

import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@NoOtherInstanceWithSameHead(message = "headId::Cette structure est déjà chapeau d'une autre instance")
@NoOtherInstanceWithSameRH(message = "rhId::Cette structure est déjà en charge des ressources humaines d'une autre instance")
@ExistingInstanceId
@CoherentHeadAndRh
public class UpdateInstanceDTO
{
    @NotNull(message = "L'instance à modifier ne peut être nul")
    private Long instanceId;
    @NotNull(message = "Le nom de l'instance ne peut être nul")
    @NotBlank(message = "Le nom de l'instance ne peut être nul")
    private String name;
    @ExistingStrId @NotNull(message = "Le chapeau de l'instance ne peut être nul")
    private Long headId;
    @ExistingStrId @NotNull(message = "La structure en charge des ressources humaines de l'instance ne peut être nul")
    private Long rhId;
}
