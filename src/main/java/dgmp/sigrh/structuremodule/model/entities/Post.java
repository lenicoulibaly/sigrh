package dgmp.sigrh.structuremodule.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.fonctionmodule.model.entities.Fonction;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Post
{
	@Id //@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
	private Long idPost;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_FONCTION")
	private Fonction fonction;
	private String libellePost;
	@ManyToOne
	@JoinColumn(name = "ID_UNITE_ADMIN")
	private Structure structure;
	@OneToOne
	@JoinColumn(name = "ID_AGENT")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Agent agent;

	public Post(Long idPost) {
		this.idPost = idPost;
	}
}
