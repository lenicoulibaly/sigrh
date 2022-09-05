package dgmp.sigrh.fonctionmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateFonctionDTO
{
    @UniqueFonctionName
    private String nomFonction;
    private boolean fonctionDeNomination;
    private boolean fonctionTopManager;
}
