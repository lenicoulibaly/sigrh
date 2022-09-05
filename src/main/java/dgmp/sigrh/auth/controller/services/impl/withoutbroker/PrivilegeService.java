package dgmp.sigrh.auth.controller.services.impl.withoutbroker;

import dgmp.sigrh.auth.controller.repositories.PrivilegeDAO;
import dgmp.sigrh.auth.controller.repositories.PrivilegeToRoleAssRepo;
import dgmp.sigrh.auth.controller.repositories.PrivilegeToUserAssRepo;
import dgmp.sigrh.auth.controller.repositories.RoleToUserAssRepo;
import dgmp.sigrh.auth.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth.controller.services.spec.IRoleService;
import dgmp.sigrh.auth.controller.services.spec.IStructureService;
import dgmp.sigrh.auth.model.dtos.appprivilege.CreatePrivilegeDTO;
import dgmp.sigrh.auth.model.dtos.appprivilege.PrivilegeMapper;
import dgmp.sigrh.auth.model.dtos.appprivilege.ReadPrivilegeDTO;
import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor @Slf4j
@Profile("withoutbroker")
public class PrivilegeService implements IPrivilegeService
{
    private final RoleToUserAssRepo rtuRepo;
    private final IRoleService roleService;
    private final PrivilegeMapper privilegeMapper;
    private final PrivilegeDAO privilegeDAO;
    private final IStructureService strService;
    //private final IBrokerMessageSender<AppPrivilege> messageSender;
    private final PrivilegeToUserAssRepo ptuRepo;
    private final PrivilegeToRoleAssRepo ptrRepo;

    @Override
    public boolean hasPrivilege(Long userId, Long privilegeId, Long strId)
    {
        if(privilegeDAO.hasImmediateAndValidPrivilege(userId, privilegeId, strId)) return true; // Si le user possède cette assignation de façon immédiate, on retourne  true
        RoleToUserAss defaultRoleAss = roleService.getUserActiveRoleAss(userId); // Sinon on recuprère son assignation par défaut
        if(defaultRoleAss==null) return false;
        return privilegeDAO.roleHasValidPrivilege(defaultRoleAss.getRole().getRoleId(), privilegeId);
    }

    @Override
    public boolean hasPrivilege(Long userId, String privilegeCode, Long strId)
    {
        AppPrivilege privilege = privilegeDAO.findByPrivilegeCode(privilegeCode);
        if(privilege==null) return false;
        return this.hasPrivilege(userId, privilege.getPrivilegeId(), strId);
    }

    @Override
    public boolean hasAnyPrivilege(Long userId, Long strId, String ...privilegeCodes)
    {
        return Arrays.stream(privilegeCodes).anyMatch(prvCode->this.hasPrivilege(userId, prvCode, strId));
    }

    @Override
    public ReadPrivilegeDTO createPrivilege(CreatePrivilegeDTO dto)
    {
        AppPrivilege privilege = privilegeMapper.getAppPrivilege(dto);
        privilege = privilegeDAO.save(privilege);
        //messageSender.sendEvent(AUTH_TOPIC, PrivilegeEventTypes.CREATE, privilege);
        return privilegeMapper.mapToReadPrivilegeDTO(privilege);
    }

    @Override
    public Set<AppPrivilege> getValidPrivilegesForUser(Long userId, Long strId)
    {
        RoleToUserAss defaultRoleAss = roleService.getUserActiveRoleAss(userId);
        Set<AppPrivilege> directPrivileges = privilegeDAO.getImmediatePrivilegesForUser(userId, strId);

        Set<AppPrivilege> defaultRolePrivileges = defaultRoleAss == null ? new HashSet<>() :
                (defaultRoleAss.getStructure() == null ? new HashSet<>() :
                (!defaultRoleAss.getStructure().getStrId().equals(strId) ? new HashSet<>() : privilegeDAO.getValidPrivilegesForRole(defaultRoleAss.getRole().getRoleId())));

        //defaultRolePrivileges = privilegeDAO.getValidPrivilegesForRole(defaultRoleAss.getRole().getRoleId());

        return Stream.concat(directPrivileges.stream(), defaultRolePrivileges.stream())
                .filter(p->!ptuRepo.privilegeIsRevokedForUser(userId, p.getPrivilegeId(), strId))
                .collect(Collectors.toSet());
    }

    @Override
    public Page<ReadPrivilegeDTO> searchPrivileges(String searchKey, Pageable pageable)
    {
        Page<AppPrivilege> rolePage = privilegeDAO.searchPrivileges(searchKey, pageable);
        List<ReadPrivilegeDTO> readRoleDTOS = rolePage.stream().map(privilegeMapper::mapToReadPrivilegeDTO).collect(Collectors.toList());
        return new PageImpl<>(readRoleDTOS, pageable, rolePage.getTotalElements());
    }
}
