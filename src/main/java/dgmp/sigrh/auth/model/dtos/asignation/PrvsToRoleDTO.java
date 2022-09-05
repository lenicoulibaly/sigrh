package dgmp.sigrh.auth.model.dtos.asignation;

import dgmp.sigrh.auth.model.dtos.approle.ExistingRoleId;
import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvsToRoleDTO
{
    @ExistingRoleId
    private Long roleId;
    private Set<Long> privilegeIds;
}
