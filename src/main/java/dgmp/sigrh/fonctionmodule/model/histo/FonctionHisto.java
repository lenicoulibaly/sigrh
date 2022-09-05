package dgmp.sigrh.fonctionmodule.model.histo;

import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.fonctionmodule.model.events.FonctionEventType;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class FonctionHisto
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long histoId;
	private Long idFonction;
	private String nomFonction;
	private boolean fonctionDeNomination;
	private boolean fonctionTopManager;
	private int rang;
	@Enumerated(value = EnumType.STRING)
	private PersistenceStatus status;
	@Enumerated(value = EnumType.STRING)
	private FonctionEventType eventType;
	@Embedded
	private EventActorIdentifier eventActorIdentifier;

	public FonctionHisto(Long histoId) {
		this.histoId = histoId;
	}
}
