package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.model.dtos.approle.ExistingRoleId;
import dgmp.sigrh.auth.model.dtos.appuser.ExistingUserId;
import dgmp.sigrh.structuremodule.model.dtos.str.ExistingStrId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoleToUserDTO
{
    @ExistingUserId
    private Long userId;
    @ExistingRoleId
    private Long roleId;
    @ExistingStrId
    private Long strId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private int assStatus;
}
