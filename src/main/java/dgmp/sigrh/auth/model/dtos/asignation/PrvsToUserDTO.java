package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.model.dtos.appuser.ExistingUserId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvsToUserDTO
{
    @ExistingUserId
    private Long userId;
    private Set<Long> privilegeIds;
    @ExistingStrId
    private Long strId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
