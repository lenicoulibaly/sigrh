package dgmp.sigrh.auth2.model.dtos.approle;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRoleDTO
{
    @UniqueRoleCode
    private String roleCode;
    @UniqueRoleName
    private String roleName;
}
