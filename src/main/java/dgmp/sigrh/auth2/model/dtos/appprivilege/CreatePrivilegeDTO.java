package dgmp.sigrh.auth2.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreatePrivilegeDTO
{
    @UniquePrvCode
    private String privilegeCode;
    @UniquePrvName
    private String privilegeName;
    @ValidPrvType
    private Long typeId;
}