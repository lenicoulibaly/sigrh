package dgmp.sigrh.structuremodule.model.entities.post;

import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostGroup
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postGroupId;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_FONCTION")
	private Fonction fonction;
	private String intitule;
	private String postDescription;
	@ManyToOne
	@JoinColumn(name = "ID_UNITE_ADMIN")
	private Structure structure;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;

	@Transient
	private List<Emploi> emploisCompatibles;

	public PostGroup(Long postGroupId) {
		this.postGroupId = postGroupId;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof PostGroup)) return false;
		PostGroup post = (PostGroup) o;
		return Objects.equals(postGroupId, post.postGroupId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(postGroupId);
	}
}
