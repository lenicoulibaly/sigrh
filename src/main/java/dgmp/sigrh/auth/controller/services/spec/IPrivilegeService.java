package dgmp.sigrh.auth.controller.services.spec;

import dgmp.sigrh.auth.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth.model.dtos.appprivilege.ReadPrivilegeDTO;
import dgmp.sigrh.auth.model.entities.AppPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IPrivilegeService
{
    boolean hasPrivilege(Long userId, Long privilegeId, Long strId);
    boolean hasPrivilege(Long userId, String privilegeCode, Long strId);

    boolean hasAnyPrivilege(Long userId, Long strId, String... privilegeCodes);

    ReadPrivilegeDTO createPrivilege(CreatePrivilegeDTO dto);
    Set<AppPrivilege> getValidPrivilegesForUser(Long userId, Long strId);
    Page<ReadPrivilegeDTO> searchPrivileges(String searchKey, Pageable pageable);
}
