package dgmp.sigrh.structuremodule.model.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdatePostDTO
{
	@NotNull(message = "L'identifiant du post ne peut être nul")
	@ExistingPostId
	private Long postId;
	@NotNull(message = "L'intitulé du poste ne peut être nul")
	private String intitule;
	private List<Long> emploisIds;
}
