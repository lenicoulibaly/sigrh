package dgmp.sigrh.auth.model.dtos.approle;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadRoleDTO
{
    private Long RoleId;
    private String roleCode;
    private String RoleName;
}
