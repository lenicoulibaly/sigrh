package dgmp.sigrh.auth.model.dtos.appprivilege;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper
{
    AppPrivilege getAppPrivilege(CreatePrivilegeDTO dto);
    ReadPrivilegeDTO mapToReadPrivilegeDTO(AppPrivilege privilege);
}
