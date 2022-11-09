package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.model.dtos.appprivilege.ExistingPrivilegeId;
import dgmp.sigrh.auth2.model.dtos.approle.ExistingRoleId;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    @ExistingPrivilegeId
    private Long privilegeId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
