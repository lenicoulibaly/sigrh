package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.model.dtos.appprivilege.ExistingPrivilegeId;
import dgmp.sigrh.auth.model.dtos.approle.ExistingRoleId;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    @ExistingPrivilegeId
    private Long privilegeId;
}
