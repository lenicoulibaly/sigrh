package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.auth2.model.enums.UserStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadUserDTO
{
    private Long userId;
    private String username;
    private String nom;
    private String matricule;
    private String password;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private UserStatus status;

    private Long strId;
    private String strName;
    private String strSigle;
    private String strHierarchySigle;
    private Long strParentId;
}
