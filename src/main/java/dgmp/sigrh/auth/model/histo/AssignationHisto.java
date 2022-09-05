package dgmp.sigrh.auth.model.histo;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.entities.AppRole;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.entities.Assignation;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.model.events.types.auth.AssignationEventTypes;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssignationHisto
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    private Long assignationHistoId;
    private Long assignationId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "PRIVILEGE_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;
    private boolean active;
    private boolean defaultFunction;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private LocalDateTime creationDate;
    private AssignationEventTypes eventType;

    private LocalDateTime mutationDate;
    private Long updaterId;
    private String updaterUsername;
    private Long updaterFunctionId;
    private String updaterFunctionCode;
    private String updaterFunctionName;

    public static AssignationHisto getAssignationHisto(Assignation assignation, EventActorIdentifier eventIdentifier, AssignationEventTypes eventType)
    {
        if(assignation == null) return null;
        AssignationHisto assignationHisto = new AssignationHisto();
        BeanUtils.copyProperties(assignation, assignationHisto);
        assignationHisto.eventType = eventType;
        if(eventIdentifier == null) return assignationHisto;

        BeanUtils.copyProperties(eventIdentifier, assignationHisto);
        assignationHisto.mutationDate = eventIdentifier.getModificationDate();

        return assignationHisto;
    }
}
