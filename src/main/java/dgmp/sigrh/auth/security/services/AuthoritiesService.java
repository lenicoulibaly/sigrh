package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.auth.controller.repositories.*;
import dgmp.sigrh.auth.controller.services.spec.IPrivilegeService;
import dgmp.sigrh.auth.controller.services.spec.IRoleService;
import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor @Slf4j
public class AuthoritiesService implements IAuthoritiesService
{
    private final PrivilegeDAO privilegeDAO;
    private final RoleDAO roleDAO;
    private final IPrivilegeService privilegeService;
    private final IRoleService roleService;
    private final RoleToUserAssRepo rtuRepo;
    private final PrivilegeToUserAssRepo ptuRepo;
    private final PrivilegeToRoleAssRepo ptrRepo;

    @Override
    public boolean userHasRole(Long idUser, Long roleId, Long strId)
    {
        return rtuRepo.userHasRole(idUser, roleId, strId);
    }

    @Override
    public boolean userHasPrivilege(Long idUser, Long idPrivilege, Long strId)
    {
        return privilegeService.hasPrivilege(idUser, idPrivilege, strId);
    }

    @Override
    public boolean isPrivilegeInvalidForUser(Long userId, Long privilegeId, Long strId)
    {
        return !privilegeDAO.isPrivilegeValidForUser(userId, privilegeId, strId);
    }

    @Override
    public boolean hasAuthority(Long userId, String authority, Long strId)
    {
        return this.getAuthorities(userId, strId).stream().anyMatch(auth->auth.equals(authority));
    }

    @Override
    public boolean hasAnyAuthority(Long userId, Long strId, String... authorities0)
    {
        Set<String> authorities1 = this.getAuthorities(userId, strId);
        return Arrays.stream(authorities0).anyMatch(auth0->authorities1.stream().anyMatch(auth0::equals));
    }



    @Override
    public Set<AppPrivilege> getUsersPrivileges(Long userId, Long strId)
    {
        return privilegeService.getValidPrivilegesForUser(userId, strId);
    }

    @Override
    public RoleToUserAss getActiveRoleAss(Long userId)
    {
        return roleService.getUserActiveRoleAss(userId);
    }

    @Override
    public Set<PrivilegeToUserAss> getNoneRevokedPrvAss(Long userId)
    {
        RoleToUserAss activeAss = roleService.getUserActiveRoleAss(userId);
        return activeAss == null ? new HashSet<>() : ptuRepo.getImmediateAndNoneRevokedPrivilegesAssForUser(userId, activeAss.getStructure().getStrId());
    }

    @Override
    public Set<String> getAuthorities(Long userId, Long strId)
    {
        RoleToUserAss defaultRoleAss = roleService.getUserActiveRoleAss(userId);
        String defaultRoleName = defaultRoleAss == null ? "ROLE_USER" : defaultRoleAss.getRole() ==  null ? null : "ROLE_" + defaultRoleAss.getRole().getRoleName();
        return Stream.concat(privilegeService.getValidPrivilegesForUser(userId, strId).stream().map(AppPrivilege::getPrivilegeCode), Stream.of(defaultRoleName)).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAuthorities(Long userId)
    {
        RoleToUserAss defaultRoleAss = roleService.getUserActiveRoleAss(userId);
        String defaultRoleName = defaultRoleAss == null ? "ROLE_USER"  : defaultRoleAss.getRole() == null ? null : "ROLE_" + defaultRoleAss.getRole().getRoleName();
        Long strId = defaultRoleAss == null ? null : defaultRoleAss.getStructure() == null ? null : defaultRoleAss.getStructure().getStrId();
        return Stream.concat(privilegeService.getValidPrivilegesForUser(userId, strId).stream().map(AppPrivilege::getPrivilegeCode), Stream.of(defaultRoleName)).collect(Collectors.toSet());
    }
}