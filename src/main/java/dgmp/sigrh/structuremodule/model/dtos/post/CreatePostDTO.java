package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.fonctionmodule.model.dtos.ExistingFonctionId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreatePostDTO
{
	@NotNull(message = "La fonction ne peut être null")
	@ExistingFonctionId
	private Long fonctionId;
	private String intitule;
	private String postDescription;
	@ExistingStrId
	private Long strId;
	private long nbrPosts;
	private Set<Long> emploisIds;
}
