package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.auth2.model.histo.PrivilegeHisto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper
{
    AppPrivilege getAppPrivilege(CreatePrivilegeDTO dto);
    ReadPrivilegeDTO mapToReadPrivilegeDTO(AppPrivilege privilege);

    PrivilegeHisto mapToPrivilegeHisto(AppPrivilege privilege, PrivilegeEventTypes eventType, EventActorIdentifier eai);
}
