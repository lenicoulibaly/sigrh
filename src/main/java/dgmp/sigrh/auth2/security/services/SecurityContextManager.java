package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.security.authentication.token.AppUsernamePasswordAuthToken;
import dgmp.sigrh.structuremodule.model.entities.structure.Structure;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component("scm")
@RequiredArgsConstructor
public class SecurityContextManager implements ISecurityContextManager
{
    private final IAuthoritiesService authService;
    private final UserRepo userRepo;
    private final AppUserDetailsService uds;
    private final AgentDAO agentDAO;
    @Override
    public void refreshSecurityContext(String username)
    {
        Long userId = userRepo.getUserIdByUsername(username);
        if(userId == null) return;
        AppUserDetails userDetails = (AppUserDetails)uds.loadUserByUsername(username);

        Set<? extends GrantedAuthority> auths = authService.getUserAuthorities(userId).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        SecurityContextHolder.getContext().setAuthentication(new AppUsernamePasswordAuthToken(username, null, auths, userDetails));
    }
    @Override
    public void refreshSecurityContext()
    {
        String username  = this.getAuthUsername();
        this.refreshSecurityContext(username);
    }
    @Override
    public Set<String> getAuthorities()
    {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    public boolean hasAuthority(String auth)
    {
        return getAuthorities().stream().anyMatch(a-> {
            if(a.startsWith("ROLE_")) return a.substring("ROLE_".length()).equals(auth);
            return a.equals(auth);
        });
    }

    @Override
    public boolean hasAnyAuthority(String... auths) {
        return Arrays.stream(auths).anyMatch(auth0->this.getAuthorities().stream().anyMatch(auth1-> {
            if(auth1.startsWith("ROLE_")) return auth1.substring("ROLE_".length()).equals(auth0);
            return auth1.equals(auth0);
        }));
    }

    @Override
    public String getAuthUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Long getAuthUserId() {
        return userRepo.getUserIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public Structure getVisibility()
    {
        Long authUserId = this.getAuthUserId();
        return authUserId == null ? null : authService.getUserVisibility(authUserId);
    }

    @Override
    public Long getVisibilityId()
    {
        Long authUserId = this.getAuthUserId();
        return authUserId == null ? null : authService.getUserVisibilityId(authUserId);
    }

    @Override
    public AppUserDetails getDetails() {
        return (AppUserDetails)uds.loadUserByUsername(this.getAuthUsername());
    }

    @Override
    public EventActorIdentifier getEventActorIdFromSCM()
    {
        AppUser user = userRepo.findByUsername(getAuthUsername()).orElse(null);

        PrincipalAss principalAss = this.getActivePrincipalAssForUser();
        Structure visibility = this.getUserVisibility();

        if(user == null) return null;
        Agent agent = user.getAgentId()==null ? null : agentDAO.findById(user.getAgentId()).orElse(null);
        return EventActorIdentifier.builder()
                .modifierUserId(user.getUserId())
                .modifierUsername(user.getUsername())
                .agentId(user.getAgentId())
                .modifierStrId(this.getVisibilityId())
                .modifierStrName(visibility == null ? null : visibility.getStrName())
                .modificationDate(LocalDateTime.now())
                .modifierAssId(principalAss == null ? null : principalAss.getAssId())
                .nom(agent == null ? null : agent.getNom())
                .prenom(agent == null ? null : agent.getPrenom())
                .matricule(agent == null ? null : agent.getMatricule())
                .build();
    }

    @Override
    public PrincipalAss getActivePrincipalAssForUser()
    {
        return authService.getActivePrincipalAssForUser(this.getAuthUserId());
    }

    @Override
    public Structure getUserVisibility()
    {
        return authService.getUserVisibility(this.getAuthUserId());
    }

    @Override
    public Long getUserVisibilityId()
    {
        return  authService.getUserVisibilityId(this.getAuthUserId());
    }

    @Override
    public boolean userHasAuthority(String authority)
    {
        return authService.userHasAuthority(this.getAuthUserId(), authority);
    }

    @Override
    public boolean userHasAnyAuthority(String... authorities)
    {
        return authService.userHasAnyAuthority(this.getAuthUserId(), authorities);
    }

    @Override
    public Set<AppPrivilege> getUsersPrivileges()
    {
        return authService.getUsersPrivileges(this.getAuthUserId());
    }

    @Override
    public Set<AppRole> getUsersRoles()
    {
        return authService.getUsersRoles(this.getAuthUserId());
    }

    @Override
    public Set<String> getUserAuthorities()
    {
        return authService.getUserAuthorities(this.getAuthUserId());
    }
}
