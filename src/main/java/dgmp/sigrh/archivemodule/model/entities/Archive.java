package dgmp.sigrh.archivemodule.model.entities;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Archive
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long archiveId;
	private String archiveNum;
	private String description;
	private String path;
	@ManyToOne @JoinColumn(name = "TYPE_ID")
	private Type archiveType;
	@ManyToOne
	@JoinColumn(name = "ID_AGENT")
	private Agent agent;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;

	@Transient
	private MultipartFile file;
}
