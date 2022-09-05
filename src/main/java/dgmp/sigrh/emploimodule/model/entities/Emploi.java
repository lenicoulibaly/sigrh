package dgmp.sigrh.emploimodule.model.entities;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Emploi 
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmploi;
	private String nomEmploi;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;
	@OneToMany(mappedBy = "emploi")
	private Collection<Agent> listAgents;

	public Emploi(Long idEmploi) {
		this.idEmploi = idEmploi;
	}
}
