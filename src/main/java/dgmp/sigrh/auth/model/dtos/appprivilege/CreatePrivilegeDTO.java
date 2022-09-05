package dgmp.sigrh.auth.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePrivilegeDTO
{
    private String privilegeCode;
    private String privilegeName;
}
