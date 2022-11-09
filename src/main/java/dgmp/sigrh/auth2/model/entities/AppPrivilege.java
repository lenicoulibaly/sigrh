package dgmp.sigrh.auth2.model.entities;

import dgmp.sigrh.auth2.model.enums.PrvGroup;
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

    @Enumerated(EnumType.STRING)
    private PrvGroup prvGroup;

    public AppPrivilege(Long privilegeId) {
        this.privilegeId = privilegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppPrivilege)) return false;
        AppPrivilege that = (AppPrivilege) o;
        return privilegeId.equals(that.privilegeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privilegeId);
    }
}
