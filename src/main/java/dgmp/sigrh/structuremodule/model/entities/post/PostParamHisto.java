package dgmp.sigrh.structuremodule.model.entities.post;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Setter
//@IdClass(PostParamId.class)
public class PostParamHisto
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long histoId;
    private Long postId;
    private Long emploiId;
    private PersistenceStatus status;
    @Enumerated(EnumType.STRING)
    private PostEventType eventType;
    @Embedded
    private EventActorIdentifier eai;
}
