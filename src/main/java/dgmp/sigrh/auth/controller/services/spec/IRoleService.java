package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth.model.dtos.approle.ReadRoleDTO;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto);
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);

    RoleToUserAss getUserActiveRoleAss(Long userId);
}
