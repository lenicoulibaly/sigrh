package dgmp.sigrh.emploimodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReadEmploiDTO
{
    private Long idEmploi;
    private String nomEmploi;
    private long nbrAgent;
    private double proportion;

    public ReadEmploiDTO(Long idEmploi, String nomEmploi) {
        this.idEmploi = idEmploi;
        this.nomEmploi = nomEmploi;
    }
}
