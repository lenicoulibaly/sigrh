package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;

import java.util.Set;

public interface IAuthoritiesService
{
    boolean principalAssHasAuthority(Long assId, String authority);
    boolean principalAssHasAnyAuthority(Long assId, String ...authorities);
    Set<String> getPrincipalAssAuthorities(Long assId);


    //=================================================================

    PrincipalAss getActivePrincipalAssForUser(Long userId);

    Long getActivePrincipalAssIdForUser(Long userId);

    Structure getUserVisibility(Long userId);
    Long getUserVisibilityId(Long userId);

    boolean userHasAuthority(Long UserId, String authority);
    boolean userHasAnyAuthority(Long userId, String ...authorities);

    Set<AppPrivilege> getUsersPrivileges(Long userId);
    Set<AppRole> getUsersRoles(Long userId);

    Set<String> getUserAuthorities(Long userId);
}
