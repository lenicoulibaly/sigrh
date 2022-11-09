package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.dtos.approle.CreateRoleDTO;
import dgmp.sigrh.auth2.model.dtos.approle.ReadRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto);
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);
}
