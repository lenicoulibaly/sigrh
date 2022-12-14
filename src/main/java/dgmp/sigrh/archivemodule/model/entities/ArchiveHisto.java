package dgmp.sigrh.archivemodule.model.entities;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.archivemodule.model.enums.ArchiveEventTypes;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ArchiveHisto
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long histoId;
	private Long archiveId;
	@Column(unique = true, nullable = true)
	private String archiveNum;
	private String description;
	private String path;
	@ManyToOne
	private Type typeArchive;
	@ManyToOne
	@JoinColumn(name = "AGENT_ID")
	private Agent agent;

	@Transient
	private MultipartFile file;

	@Enumerated(EnumType.STRING)
	private ArchiveEventTypes eventType;
	@Embedded
	private EventActorIdentifier eai;
}
