package dgmp.sigrh.fonctionmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueFonctionName(message = "nomFonction::Ce nom de fonction existe déjà")
public class UpdateFonctionDTO
{
    @ExistingFonctionId
    private Long idFonction;
    private String nomFonction;
    private boolean fonctionDeNomination;
    private boolean fonctionTopManager;
}
