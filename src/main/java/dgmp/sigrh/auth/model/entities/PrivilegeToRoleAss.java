package dgmp.sigrh.auth.model.entities;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRIVILEGE_TO_ROLE")
public class PrivilegeToRoleAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "PRIVILEGE_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivilegeToRoleAss that = (PrivilegeToRoleAss) o;
        return privilege.equals(that.privilege) && role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privilege, role);
    }
}
