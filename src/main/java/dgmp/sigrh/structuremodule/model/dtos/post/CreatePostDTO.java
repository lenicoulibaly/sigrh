package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.fonctionmodule.model.dtos.ExistingFonctionId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @UniquePostManagerByStr
public class CreatePostDTO
{
	@NotNull(message = "La fonction ne peut être null")
	@ExistingFonctionId
	private Long fonctionId;
	private String intitule;
	private String postDescription;
	@ExistingStrId
	private Long strId;
	@Positive(message = "Le nombre de postes ne peut être négatif")
	private long nbrPosts;
	private Set<Long> emploisIds;
}
