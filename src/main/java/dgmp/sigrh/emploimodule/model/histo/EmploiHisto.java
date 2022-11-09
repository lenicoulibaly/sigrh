package dgmp.sigrh.emploimodule.model.histo;

import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.emploimodule.model.events.EmploiEventType;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmploiHisto
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long histoId;
	private Long idEmploi;
	private String nomEmploi;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;
	@Enumerated(value = EnumType.STRING)
	private EmploiEventType eventType;
	@Embedded
	private EventActorIdentifier eai;

	public EmploiHisto(Long histoId) {
		this.histoId = histoId;
	}
}
