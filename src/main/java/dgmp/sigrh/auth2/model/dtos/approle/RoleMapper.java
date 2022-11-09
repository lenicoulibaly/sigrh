package dgmp.sigrh.auth2.model.dtos.approle;

import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.RoleEventTypes;
import dgmp.sigrh.auth2.model.histo.RoleHisto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    AppRole mapToRole(CreateRoleDTO dto);
    ReadRoleDTO mapToReadRoleDTO(AppRole role);
    RoleHisto mapToRoleHisto(AppRole appRole, RoleEventTypes eventType, EventActorIdentifier eai);
}
