package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReinitialisePasswordDTO
{
    private String username;
    private String newPassword;
    private String confirmNewPassword;
    private String passwordReinitialisationToken;
}
