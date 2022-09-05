package dgmp.sigrh.emploimodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Setter @Getter
@UniqueEmploiName(message = "nomEmploi::Ce nom d'emploi existe déjà")
public class UpdateEmploiDTO
{
    @ExistingEmploiId(message = "Cet emploi est inexistant")
    private Long idEmploi;
    private String nomEmploi;
}
