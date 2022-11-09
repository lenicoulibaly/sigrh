package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.model.dtos.approle.ExistingRoleId;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvsToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    private Set<Long> prvIds;
    private LocalDate startsAt;
    private LocalDate endsAt;
}
