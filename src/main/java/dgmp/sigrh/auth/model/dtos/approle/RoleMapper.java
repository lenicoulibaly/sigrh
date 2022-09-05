package dgmp.sigrh.auth.model.dtos.approle;

import dgmp.sigrh.auth.model.entities.AppRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    AppRole getAppRole(CreateRoleDTO dto);
    ReadRoleDTO getReadRoleDTO(AppRole role);
}
