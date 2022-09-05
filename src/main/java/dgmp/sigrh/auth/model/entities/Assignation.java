package dgmp.sigrh.auth.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Inheritance(strategy = InheritanceType.SINGLE_TABLE) @DiscriminatorColumn(name = "ASS_TYPE")
public class Assignation
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    protected Long assignationId;
    protected int assStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected boolean defaultAss;
    protected LocalDateTime startsAt;
    protected LocalDateTime endsAt;
    protected LocalDateTime creationDate;
    protected LocalDateTime lastModificationDate;

    public Assignation(Long assignationId) {
        this.assignationId = assignationId;
    }
}
