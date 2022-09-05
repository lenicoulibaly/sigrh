package dgmp.sigrh.auth.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@NoneExistingUserName @NoneExistingEmail @NoneExistingTel
public class UpdateUserDTO
{
    @ExistingUserId
    private Long userId;
    private String username;
    private String email;
    private String tel;
}
