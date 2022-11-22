package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor //@AllArgsConstructor @Builder
public class ReadPrincipalAssDTO
{
    private Long assId;
    private String intitule;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private int assStatus;
    private String status;
    private String username;
    private String strName;
    private String strSigle;
    private String hierarchySigles;
    private List<Structure> hierarchy;


    public ReadPrincipalAssDTO(Long assId, String intitule, LocalDate startsAt, LocalDate endsAt, int assStatus, String status, String username, String strName, String strSigle, String hierarchySigles) {
        this.assId = assId;
        this.intitule = intitule;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.assStatus = assStatus;
        this.status = status;
        this.username = username;
        this.strName = strName;
        this.strSigle = strSigle;
        this.hierarchySigles = hierarchySigles;
    }
}
