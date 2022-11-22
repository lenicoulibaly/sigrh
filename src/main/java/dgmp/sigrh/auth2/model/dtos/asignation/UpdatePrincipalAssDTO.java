package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.model.dtos.appuser.ExistingUserId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentDates
public class UpdatePrincipalAssDTO
{
    @ExistingAssId
    private Long assId;
    @ExistingUserId
    private Long userId;
    @ExistingStrId
    private Long strId;
    private String intitule;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<Long> roleIds = new HashSet<>();
    private Set<Long> prvIds = new HashSet<>();
}
