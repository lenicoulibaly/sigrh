package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.auth2.model.histo.PrivilegeHisto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper
{
    @Mapping(target = "prvType.typeId", source = "typeId")
    AppPrivilege getAppPrivilege(CreatePrivilegeDTO dto);
    @Mapping(target = "prvTypeName", source = "prvType.name")
    ReadPrivilegeDTO mapToReadPrivilegeDTO(AppPrivilege privilege);

    PrivilegeHisto mapToPrivilegeHisto(AppPrivilege privilege, PrivilegeEventTypes eventType, EventActorIdentifier eai);
}
