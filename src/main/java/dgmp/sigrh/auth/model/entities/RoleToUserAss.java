package dgmp.sigrh.auth.model.entities;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("ROLE_TO_USER")
public class RoleToUserAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    @ManyToOne @JoinColumn(name = "structure_id")
    private Structure structure;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleToUserAss that = (RoleToUserAss) o;
        return user.equals(that.user) && role.equals(that.role) && structure.equals(that.structure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, role, structure);
    }
}
