package dgmp.sigrh.emploimodule.model.entities;

import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Emploi)) return false;
		Emploi emploi = (Emploi) o;
		return Objects.equals(idEmploi, emploi.idEmploi);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idEmploi);
	}

	public Emploi(Long idEmploi) {
		this.idEmploi = idEmploi;
	}
}
