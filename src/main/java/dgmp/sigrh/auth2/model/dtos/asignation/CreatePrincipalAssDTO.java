package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.model.dtos.appuser.ExistingUserId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePrincipalAssDTO
{
    @ExistingUserId
    private Long userId;
    @ExistingStrId
    private Long strId;
    private String intitule;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<Long> roleIds;
    private Set<Long> prvIds;
}
