package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.ReadPrivilegeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPrivilegeService
{
    ReadPrivilegeDTO createPrivilege(CreatePrivilegeDTO dto);
    Page<ReadPrivilegeDTO> searchPrivileges(String searchKey, Pageable pageable);
}
