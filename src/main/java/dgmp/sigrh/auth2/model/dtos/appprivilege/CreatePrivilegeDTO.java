package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.model.enums.PrvGroup;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePrivilegeDTO
{
    @UniquePrvCode
    private String privilegeCode;
    @UniquePrvName
    private String privilegeName;
    private PrvGroup prvGroup;
}