package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueUsername(message = "username::Login déjà attribué") @ValidToken(message = "activationToken::Lien invalide") @ConcordantPassword
public class ActivateAccountDTO
{
    @Id
    private Long userId;
    @NotBlank(message = "Le login ne peut être nul")
    @Length(message = "Le login doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le login ne peut être nul")
    private String username;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Length(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    private String password;
    private String confirmPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String activationToken;
}
