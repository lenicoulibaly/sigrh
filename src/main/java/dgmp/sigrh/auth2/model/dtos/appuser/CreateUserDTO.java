package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateUserDTO
{
    //@UniqueUsername
    //private String username;
    @UniqueEmail
    private String email;
    @UniqueTel
    private String tel;
    @ExistingOrNullUserId
    private Long strId;
}
