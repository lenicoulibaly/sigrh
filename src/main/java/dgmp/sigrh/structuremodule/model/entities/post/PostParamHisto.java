package dgmp.sigrh.structuremodule.model.entities.post;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.embedded.PostParamId;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Setter
@IdClass(PostParamId.class)
public class PostParamHisto
{
    @Id
    private Long postId;
    @Id
    private Long emploiId;
    private PersistenceStatus status;
    @Enumerated(EnumType.STRING)
    private PostEventType eventType;
    @Embedded
    private EventActorIdentifier eai;
}
