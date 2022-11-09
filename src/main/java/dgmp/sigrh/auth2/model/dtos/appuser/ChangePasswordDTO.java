package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChangePasswordDTO
{
    private Long userId;
    private String username;
    private String oldPassword;
    private String newPassword;
    private String rePassword;
}
