package dgmp.sigrh.auth.model.histo;

import dgmp.sigrh.auth.model.entities.AppRole;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.auth.RoleEventTypes;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoleHisto
{
    @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long roleHistoId;
    private Long roleId;
    private String roleCode;
    private String roleName;
    private RoleEventTypes eventType;

    private LocalDateTime mutationDate;
    private Long updaterId;
    private String updaterUsername;
    private Long updaterFunctionId;
    private String updaterFunctionCode;
    private String updaterFunctionName;

    public static RoleHisto getRoleHisto(AppRole role, EventActorIdentifier eventIdentifier, RoleEventTypes eventType)
    {
        if(role == null) return null;
        RoleHisto roleHisto = new RoleHisto();
        BeanUtils.copyProperties(role, roleHisto);
        roleHisto.eventType = eventType;
        if(eventIdentifier == null) return roleHisto;

        BeanUtils.copyProperties(eventIdentifier, roleHisto);
        roleHisto.mutationDate = eventIdentifier.getModificationDate();

        return roleHisto;
    }
}
