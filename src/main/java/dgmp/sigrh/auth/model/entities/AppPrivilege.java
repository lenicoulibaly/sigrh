package dgmp.sigrh.auth.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class AppPrivilege
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long privilegeId;
    @Column(unique = true)
    private String privilegeCode;
    @Column(unique = true)
    private String privilegeName;

    @ManyToOne @JoinColumn(name = "privilege_group_id")
    private PrivilegeGroup privilegeGroup;

    public AppPrivilege(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppPrivilege)) return false;
        AppPrivilege that = (AppPrivilege) o;
        return Objects.equals(getPrivilegeId(), that.getPrivilegeId()) || Objects.equals(getPrivilegeCode(), that.getPrivilegeCode()) || Objects.equals(getPrivilegeName(), that.getPrivilegeName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrivilegeId(), getPrivilegeCode(), getPrivilegeName());
    }
}
