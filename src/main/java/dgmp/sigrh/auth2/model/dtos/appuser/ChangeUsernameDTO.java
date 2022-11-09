package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangeUsernameDTO
{
    private Long userId;
    private String oldUsername;
    private String newUsername;
}
