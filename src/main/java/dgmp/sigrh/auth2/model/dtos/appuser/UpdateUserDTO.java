package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueUsername
@UniqueEmail
@UniqueTel
public class UpdateUserDTO
{
    @ExistingUserId
    private Long userId;
    private String username;
    private String email;
    private String tel;
}
