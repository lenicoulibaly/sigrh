package dgmp.sigrh.structuremodule.model.entities.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import dgmp.sigrh.structuremodule.model.events.PostEventType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostHisto
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long histoId;
	private Long postId;
	@ManyToOne
	@JoinColumn(name = "ID_UNITE_ADMIN")
	private Structure structure;
	@OneToOne
	@JoinColumn(name = "ID_AGENT")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Agent agent;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;

	@ManyToOne
	@JoinColumn(name = "POST_GROUP_ID")
	private PostGroup postGroup;
	private boolean vacant;

	@Enumerated(EnumType.STRING)
	private PostEventType eventType;
	@Embedded
	private EventActorIdentifier eai;


}
