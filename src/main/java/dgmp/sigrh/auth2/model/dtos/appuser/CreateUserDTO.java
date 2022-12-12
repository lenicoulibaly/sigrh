package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.agentmodule.model.dtos.ExistingOrNullAgtId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingOrNullStrId;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserDTO
{
    @UniqueEmail(message = "Adresse mail déjà attribuée")
    private String email;
    @UniqueTel(message = "N° de téléphone déjà attribué")
    private String tel;
    @ExistingOrNullStrId
    private Long strId;

    @ExistingOrNullAgtId
    private Long agentId;
}
