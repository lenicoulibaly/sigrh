package dgmp.sigrh.auth2.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth2.controller.repositories.PrivilegeHistoRepo;
import dgmp.sigrh.auth2.controller.repositories.PrvAssRepo;
import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import dgmp.sigrh.auth2.controller.repositories.PrvToRoleAssRepo;
import dgmp.sigrh.auth2.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth2.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.PrivilegeMapper;
import dgmp.sigrh.auth2.model.dtos.appprivilege.ReadPrivilegeDTO;
import dgmp.sigrh.auth2.model.dtos.appprivilege.SelectedPrvDTO;
import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.events.types.auth.PrivilegeEventTypes;
import dgmp.sigrh.auth2.model.histo.PrivilegeHisto;
import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import dgmp.sigrh.shared.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class PrivilegeService implements IPrivilegeService
{
    private final PrvRepo prvRepo;
    private final PrvAssRepo prvAssRepo;
    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final PrivilegeHistoRepo prvHistoRepo;
    private final PrivilegeMapper prvMapper;
    private final ISecurityContextManager scm;

    @Override @Transactional
    public ReadPrivilegeDTO createPrivilege(CreatePrivilegeDTO dto)
    {
        AppPrivilege privilege = prvMapper.getAppPrivilege(dto);
        privilege = prvRepo.save(privilege);
        PrivilegeHisto histo = prvMapper.mapToPrivilegeHisto(privilege, PrivilegeEventTypes.CREATE, scm.getEventActorIdFromSCM());
        prvHistoRepo.save(histo);
        return prvMapper.mapToReadPrivilegeDTO(privilege);
    }

    @Override
    public Page<ReadPrivilegeDTO> searchPrivileges(String searchKey, Pageable pageable)
    {
        Page<AppPrivilege> rolePage = prvRepo.searchPrivileges(StringUtils.stripAccentsToUpperCase(searchKey), pageable);
        List<ReadPrivilegeDTO> readRoleDTOS = rolePage.stream().map(prvMapper::mapToReadPrivilegeDTO).collect(Collectors.toList());
        return new PageImpl<>(readRoleDTOS, pageable, rolePage.getTotalElements());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Set<Long> roleIds)
    {
        Set<Long> selectedPrvIds = prvToRoleAssRepo.findActivePrvIdsForRoles(roleIds);
        return prvRepo.findAll().stream().map(prv->
                new SelectedPrvDTO(prv.getPrivilegeId(),
                        prv.getPrivilegeCode(), prv.getPrivilegeName(),
                        selectedPrvIds.contains(prv.getPrivilegeId()),
                        selectedPrvIds.contains(prv.getPrivilegeId()))).collect(Collectors.toList());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Long prAssId, Set<Long> oldRoleIds, Set<Long> roleIds, Set<Long> prvIds)
    {
        Set<Long> ownedPrvIds = prvAssRepo.getPrincipalAssPrvIds(prAssId);
        //List<AppPrivilege> allPrvs = prvRepo.findAll();
        Set<Long> selectedPrvIds = prvToRoleAssRepo.findActivePrvIdsForRoles(roleIds);

        //Set<Long> addedRoleIds = roleIds.stream().filter(rId-> !oldRoleIds.contains(rId)).collect(Collectors.toSet());
        Set<Long> retiredRoleIds = oldRoleIds.stream().filter(rId-> !roleIds.contains(rId)).collect(Collectors.toSet());
        Set<Long> prvIdsToBeRetired = retiredRoleIds == null ? new HashSet<>() : retiredRoleIds.isEmpty() ? new HashSet<>() : prvToRoleAssRepo.findActivePrvIdsForRoles(retiredRoleIds).stream().filter(prvId->!selectedPrvIds.contains(prvId)).collect(Collectors.toSet());
        prvIds = prvIds.stream().filter(prvId->!prvIdsToBeRetired.contains(prvId)).collect(Collectors.toSet());
        selectedPrvIds.addAll(prvIds);
        return prvRepo.findAll().stream().map(prv->new SelectedPrvDTO(prv.getPrivilegeId(), prv.getPrivilegeCode(), prv.getPrivilegeName(), selectedPrvIds.contains(prv.getPrivilegeId()), ownedPrvIds.contains(prv.getPrivilegeId()))).collect(Collectors.toList());
    }
}
