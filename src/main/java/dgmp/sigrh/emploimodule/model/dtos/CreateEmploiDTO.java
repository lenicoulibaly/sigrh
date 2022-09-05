package dgmp.sigrh.emploimodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateEmploiDTO
{
    @UniqueEmploiName
    @NotNull(message = "Le nom de l'emploi ne peut être nul")
    @Size(min = 3, message = "Le nom de l'emploi ne peut être inférieur à 3")
    private String nomEmploi;
}
