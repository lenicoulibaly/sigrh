package dgmp.sigrh.auth2.model.entities;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppUser
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long userId;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private Long agentId;
    private Long defaultAssId;
    @ManyToOne @JoinColumn(name = "STRUCTURE_ID")
    private Structure structure;

    public AppUser(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(getUserId(), appUser.getUserId()) || Objects.equals(getUsername(), appUser.getUsername()) || Objects.equals(getEmail(), appUser.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getEmail());
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", active=" + active +
                ", notBlocked=" + notBlocked +
                ", creationDate=" + creationDate +
                ", lastModificationDate=" + lastModificationDate +
                ", agentId=" + agentId +
                ", structure=" + structure +
                '}';
    }
}
