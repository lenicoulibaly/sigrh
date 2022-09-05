package dgmp.sigrh.auth.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserDTO
{
    @NoneExistingUserName
    private String username;
    @NoneExistingEmail
    private String email;
    @NoneExistingTel
    private String tel;
    private Long strId;
}
