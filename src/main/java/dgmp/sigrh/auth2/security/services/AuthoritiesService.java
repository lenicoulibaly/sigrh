package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.auth2.controller.repositories.PrincipalAssRepo;
import dgmp.sigrh.auth2.controller.repositories.PrvAssRepo;
import dgmp.sigrh.auth2.controller.repositories.RoleAssRepo;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
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
    private final RoleAssRepo roleAssRepo;
    private final PrvAssRepo prvAssRepo;
    private final PrincipalAssRepo principalAssRepo;
    private final UserRepo userRepo;
    @Override
    public boolean principalAssHasAuthority(Long assId, String authority)
    {
        if(assId == null || authority == null) return false;
        return roleAssRepo.principalAssHasRole(assId, authority) || prvAssRepo.principalAssHasPrivilegeCode(assId, authority);
    }

    @Override
    public boolean principalAssHasAnyAuthority(Long assId, String... authorities)
    {
        if(assId == null || authorities == null) return false;
        return roleAssRepo.principalAssHasAnyRoleName(assId, Arrays.stream(authorities).collect(Collectors.toList())) || prvAssRepo.principalAssHasAnyPrivilegeCode(assId, Arrays.stream(authorities).collect(Collectors.toList()));
    }

    @Override
    public Set<String> getPrincipalAssAuthorities(Long assId)
    {
        if(assId == null) return new HashSet<>();
        if(!principalAssRepo.principalAssHasValidDates(assId)) return new HashSet<>();
        return Stream.concat(roleAssRepo.getPrincipalAssRoles(assId).stream().map(AppRole::getRoleName),
                prvAssRepo.getPrincipalAssPrivileges(assId).stream().map(AppPrivilege::getPrivilegeCode))
                .collect(Collectors.toSet());
    }

    @Override
    public PrincipalAss getActivePrincipalAssForUser(Long userId)
    {
        if(userId == null) return null;
        if(!userRepo.existsById(userId)) return null;
        Set<PrincipalAss> principalAsses = principalAssRepo.findActiveByUser(userId);
        if(principalAsses == null) return null;
        if(principalAsses.size() == 0) return null;
        return principalAssRepo.findActiveByUser(userId).iterator().next();
    }

    @Override
    public Long getActivePrincipalAssIdForUser(Long userId)
    {
        if(userId == null) return null;
        PrincipalAss principalAss = this.getActivePrincipalAssForUser(userId);
        return principalAss == null ? null : principalAss.getAssId();
    }

    @Override
    public Structure getUserVisibility(Long userId)
    {
        if(userId == null) return null;
        PrincipalAss principalAss = this.getActivePrincipalAssForUser(userId);
        return principalAss == null ? null : principalAss.getStructure();
    }

    @Override
    public Long getUserVisibilityId(Long userId)
    {
        if(userId == null) return null;
        Structure visibility = this.getUserVisibility(userId);
        return visibility == null ? null : visibility.getStrId();
    }

    @Override
    public boolean userHasAuthority(Long userId, String authority)
    {
        if(userId == null || authority == null) return false;
        Long assId = this.getActivePrincipalAssIdForUser(userId);
        return assId == null ? false : this.principalAssHasAuthority(assId, authority);
    }

    @Override
    public boolean userHasAnyAuthority(Long userId, String... authorities)
    {
        if(userId == null || authorities == null) return false;
        Long assId = this.getActivePrincipalAssIdForUser(userId);
        return assId == null ? false : this.principalAssHasAnyAuthority(assId, authorities);
    }

    @Override
    public Set<AppPrivilege> getUsersPrivileges(Long userId)
    {
        if(userId == null) return new HashSet<>();
        Long assId = this.getActivePrincipalAssIdForUser(userId);
        return assId == null ? new HashSet<>() : prvAssRepo.getPrincipalAssPrivileges(assId);
    }

    @Override
    public Set<AppRole> getUsersRoles(Long userId)
    {
        if(userId == null) return new HashSet<>();
        Long assId = this.getActivePrincipalAssIdForUser(userId);
        return assId == null ? new HashSet<>() : roleAssRepo.getPrincipalAssRoles(assId);
    }

    @Override
    public Set<String> getUserAuthorities(Long userId)
    {
        if(userId == null) return new HashSet<>();
        Long assId = this.getActivePrincipalAssIdForUser(userId);
        return assId == null ? new HashSet<>() : this.getPrincipalAssAuthorities(assId);
    }
}