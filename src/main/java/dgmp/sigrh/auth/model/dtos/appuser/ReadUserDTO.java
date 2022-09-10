package dgmp.sigrh.auth.model.dtos.appuser;

import dgmp.sigrh.structuremodule.model.dtos.str.ReadStrDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadUserDTO
{
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String tel;
    private boolean active;
    private boolean notBlocked;
    private Long strId;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private ReadStrDTO structureDTO;
}
