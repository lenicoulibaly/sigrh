package dgmp.sigrh.structuremodule.model.entities.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.shared.model.enums.PersistenceStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Post
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	@ManyToOne
	@JoinColumn(name = "POST_GROUP_ID")
	private PostGroup postGroup;
	@OneToOne
	@JoinColumn(name = "ID_AGENT")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Agent agent;
	@Enumerated(EnumType.STRING)
	private PersistenceStatus status;
	private boolean vacant;

	public Post(Long postId) {
		this.postId = postId;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Post)) return false;
		Post post = (Post) o;
		return Objects.equals(postId, post.postId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(postId);
	}
}
