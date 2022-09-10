package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.model.dtos.appprivilege.ExistingPrivilegeId;
import dgmp.sigrh.auth.model.dtos.appuser.ExistingUserId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToUserDTO
{
    @ExistingUserId
    private Long userId;
    @ExistingPrivilegeId
    private Long privilegeId;
    @ExistingStrId
    private Long strId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
