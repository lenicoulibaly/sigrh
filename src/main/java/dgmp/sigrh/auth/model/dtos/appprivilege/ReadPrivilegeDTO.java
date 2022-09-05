package dgmp.sigrh.auth.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPrivilegeDTO
{
    private Long privilegeId;
    private String privilegeCode;
    private String privilegeName;
}
