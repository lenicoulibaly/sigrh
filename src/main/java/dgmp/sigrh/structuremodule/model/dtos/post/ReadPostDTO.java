package dgmp.sigrh.structuremodule.model.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPostDTO
{
    private Long postId;

    private String nomFonction;
    private String intitule;

    private String strName;
    private String strSigle;

    private String agentNom;
    private String agentPrenom;
    private String agentMatricule;

    private Set<String> emploisCompatibles;
}
