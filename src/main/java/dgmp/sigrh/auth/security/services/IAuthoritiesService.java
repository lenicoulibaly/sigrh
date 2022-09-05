package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.auth.model.entities.AppPrivilege;
import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;

import java.util.Set;

public interface IAuthoritiesService
{
    boolean userHasRole(Long idUser, Long roleId, Long strId);
    boolean userHasPrivilege(Long idUser, Long idPrivilege, Long strId);

    boolean isPrivilegeInvalidForUser(Long userId, Long privilegeId, Long strId);

    boolean hasAuthority(Long idUser, String authority, Long strId);
    boolean hasAnyAuthority(Long idUser, Long strId, String ...authorities);
    Set<AppPrivilege> getUsersPrivileges(Long userId, Long strId);
    RoleToUserAss getActiveRoleAss(Long userId);
    Set<PrivilegeToUserAss> getNoneRevokedPrvAss(Long userId);
    Set<String> getAuthorities(Long userId, Long strId);

    Set<String> getAuthorities(Long userId);
}
