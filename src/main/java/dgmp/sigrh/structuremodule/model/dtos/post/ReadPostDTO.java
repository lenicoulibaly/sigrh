package dgmp.sigrh.structuremodule.model.dtos.post;

import dgmp.sigrh.emploimodule.model.entities.Emploi;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPostDTO
{
    private Long postGroupId;
    private Long postId;
    private String postCode;
    private String nomFonction;
    private String intitule;
    private String postDescription;

    private String strName;
    private String strSigle;
    private long strId;

    private String agentNom;
    private String agentPrenom;
    private String agentMatricule;
    private long nbrPosts;
    private long nbrPostsVacants;
    private long nbrPostsOccupes;

    private List<Structure> hierarchy;
    private Set<Emploi> emploisCompatibles;
}
