package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.fonctionmodule.model.dtos.ExistingFonctionId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreatePostDTO
{
	@NotNull(message = "La fonction ne peut être null")
	@ExistingFonctionId
	private Long fonctionId;
	private String intitule;
	@ExistingStrId
	private Long strId;
	private List<Long> emploisIds;
}
