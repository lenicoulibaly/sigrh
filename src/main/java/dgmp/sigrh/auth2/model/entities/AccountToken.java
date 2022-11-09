package dgmp.sigrh.auth2.model.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountToken
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long tokenId;
    @Column(unique = true)
    private String token;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private LocalDateTime usageDate;
    private boolean alreadyUsed;
    private Long agentId; // Très important pour lier le token à un agent (acteur) au cas où user est null
    private String password; // Mot de passe d'utilisation du token aléatoirement généré!
    private boolean emailSent;
    @ManyToOne
    private AppUser user;

    public AccountToken(Long tokenId)
    {
        this.tokenId = tokenId;
    }

    public AccountToken(String token, AppUser user)
    {
        this.token = token;
        this.emailSent = false;
        this.alreadyUsed = false;
        this.user = user;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusDays(1);
    }
}
