package dgmp.sigrh.auth2.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppRole
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long roleId;
    @Column(unique = true)
    private String roleCode;
    @Column(unique = true)
    private String roleName;

    public AppRole(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppRole)) return false;
        AppRole appRole = (AppRole) o;
        return Objects.equals(getRoleId(), appRole.getRoleId()) || Objects.equals(getRoleCode(), appRole.getRoleCode()) || Objects.equals(getRoleName(), appRole.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleId(), getRoleCode(), getRoleName());
    }
}
