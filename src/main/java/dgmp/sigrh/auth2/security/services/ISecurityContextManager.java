package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;

import java.util.Set;

public interface ISecurityContextManager
{
    void refreshSecurityContext(String username);
    void refreshSecurityContext();
    Set<String> getAuthorities();
    boolean hasAuthority(String auth);
    boolean hasAnyAuthority(String ...auths);

    String getAuthUsername();

    Long getAuthUserId();

    Structure getVisibility();

    Long getVisibilityId();

    AppUserDetails getDetails();

    EventActorIdentifier getEventActorIdFromSCM();


    //===============================
    PrincipalAss getActivePrincipalAssForUser();
    Structure getUserVisibility();
    Long getUserVisibilityId();

    boolean userHasAuthority(String authority);
    boolean userHasAnyAuthority(String ...authorities);

    Set<AppPrivilege> getUsersPrivileges();
    Set<AppRole> getUsersRoles();

    Set<String> getUserAuthorities();
}
