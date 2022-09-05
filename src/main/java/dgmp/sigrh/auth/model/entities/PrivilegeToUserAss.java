package dgmp.sigrh.auth.model.entities;

import dgmp.sigrh.structuremodule.model.entities.Structure;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRIVILEGE_TO_USER")
public class PrivilegeToUserAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "PRIVILEGE_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "structure_id")
    private Structure structure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivilegeToUserAss that = (PrivilegeToUserAss) o;
        return user.equals(that.user) && privilege.equals(that.privilege) && structure.equals(that.structure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, privilege, structure);
    }
}
