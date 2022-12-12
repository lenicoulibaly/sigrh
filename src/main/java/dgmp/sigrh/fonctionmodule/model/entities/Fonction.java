package dgmp.sigrh.fonctionmodule.model.entities;

import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Fonction 
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFonction;
	private String nomFonction;
	private String codeFonction;
	private boolean fonctionDeNomination;
	private boolean fonctionTopManager;
	private int rang;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status; //1 == actif, 2 == supprimé, 3 == historisé

	public Fonction(Long idFonction) {
		this.idFonction = idFonction;
	}
}
