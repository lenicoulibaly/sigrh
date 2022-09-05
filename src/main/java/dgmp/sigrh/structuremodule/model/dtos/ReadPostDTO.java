package dgmp.sigrh.structuremodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPostDTO
{
    private Long idPost;

    private Long idFonction;
    private String nomFonction;

    private String libellePost;

    private long strId;
    private String strName;
    private String strSigle;

    private long idAgent;
    private String nom;
    private String prenom;
    private String matricule;
}
