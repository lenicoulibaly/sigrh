package dgmp.sigrh.auth.model.dtos.approle;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRoleDTO
{
    private String roleCode;
    private String RoleName;
}
