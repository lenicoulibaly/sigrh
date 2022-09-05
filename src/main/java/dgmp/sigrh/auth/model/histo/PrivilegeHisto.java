package dgmp.sigrh.auth.model.histo;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.auth.PrivilegeEventTypes;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrivilegeHisto
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long privilegeHistoId;
    private Long privilegeId;
    @Column(unique = true)
    private String privilegeCode;
    @Column(unique = true)
    private String privilegeName;
    private PrivilegeEventTypes eventType;

    private LocalDateTime mutationDate;
    private Long updaterId;
    private String updaterUsername;
    private Long updaterFunctionId;
    private String updaterFunctionCode;
    private String updaterFunctionName;

    public static PrivilegeHisto getPrivilegeHisto(AppPrivilege privilege, EventActorIdentifier eventIdentifier, PrivilegeEventTypes eventType)
    {
        if(privilege == null) return null;
        PrivilegeHisto privilegeHisto = new PrivilegeHisto();
        BeanUtils.copyProperties(privilege, privilegeHisto);
        privilegeHisto.eventType = eventType;
        if(eventIdentifier == null) return privilegeHisto;

        BeanUtils.copyProperties(eventIdentifier, privilegeHisto);
        privilegeHisto.mutationDate = eventIdentifier.getModificationDate();

        return privilegeHisto;
    }
}
