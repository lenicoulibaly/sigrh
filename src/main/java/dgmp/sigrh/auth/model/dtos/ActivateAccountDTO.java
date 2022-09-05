package dgmp.sigrh.auth.model.dtos;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivateAccountDTO
{
    private String username;
    private String password;
    private String confirmPassword;
    private String activationToken;
}
