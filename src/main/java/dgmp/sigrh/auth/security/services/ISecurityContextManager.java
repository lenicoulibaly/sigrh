package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;

import java.util.Set;

public interface ISecurityContextManager
{
    void refreshSecurityContext(String username);
    void refreshSecurityContext();
    Set<String> getAuthorities();
    boolean hasAuthority(String auth);
    boolean hasAnyAuthority(String ...auths);

    String getAuthUsername();
    RoleToUserAss getCurrentRoleAss();
    AppUserDetails getDetails();

    EventActorIdentifier getEventActorIdFromSCM();
}
