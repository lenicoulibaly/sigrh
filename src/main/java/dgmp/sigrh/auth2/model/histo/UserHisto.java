package dgmp.sigrh.auth2.model.histo;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.UserEventTypes;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class UserHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    @ManyToOne @JoinColumn(name = "STRUCTURE_ID")
    private Structure structure;

    @Embedded
    private EventActorIdentifier eai;
    @Enumerated(EnumType.STRING)
    private UserEventTypes eventType;


}
