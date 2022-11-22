package dgmp.sigrh.auth2.controller.services.spec;

import dgmp.sigrh.auth2.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.ReadPrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.SelectedPrvDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IPrivilegeService
{
    ReadPrivilegeDTO createPrivilege(CreatePrivilegeDTO dto);
    Page<ReadPrivilegeDTO> searchPrivileges(String searchKey, Pageable pageable);
    List<SelectedPrvDTO> getSelectedPrvs(Set<Long> roleIds);
    List<SelectedPrvDTO> getSelectedPrvs(Long prAssId, Set<Long> oldRoleIds, Set<Long> roleIds, Set<Long> prvIds);
}
