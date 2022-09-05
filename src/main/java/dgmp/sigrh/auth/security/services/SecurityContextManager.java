package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import dgmp.sigrh.auth.model.events.EventActorIdentifier;
import dgmp.sigrh.auth.security.authentication.token.AppUsernamePasswordAuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("scm")
@RequiredArgsConstructor
public class SecurityContextManager implements ISecurityContextManager
{
    private final IAuthoritiesService authService;
    private final UserDAO userDAO;
    private final AppUserDetailsService uds;
    private final AgentDAO agentDAO;
    @Override
    public void refreshSecurityContext(String username)
    {
        Optional<AppUser> user$ = userDAO.findByUsername(username);
        if(!user$.isPresent()) return;
        AppUserDetails userDetails = (AppUserDetails)uds.loadUserByUsername(username);

        Set<? extends GrantedAuthority> auths = authService.getAuthorities(user$.get().getUserId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
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
    public RoleToUserAss getCurrentRoleAss()
    {
        Optional<AppUser> user$ = userDAO.findByUsername(getAuthUsername());
        return !user$.isPresent() ? null : authService.getActiveRoleAss(user$.get().getUserId());
    }

    @Override
    public AppUserDetails getDetails() {
        return (AppUserDetails)uds.loadUserByUsername(this.getAuthUsername());
    }

    @Override
    public EventActorIdentifier getEventActorIdFromSCM()
    {
        AppUser user = userDAO.findByUsername(getAuthUsername()).orElse(null);

        RoleToUserAss activeRoleAss = this.getCurrentRoleAss();

        if(user == null) return null;
        Agent agent = user.getAgentId()==null ? null : agentDAO.findById(user.getAgentId()).orElse(null);
        return EventActorIdentifier.builder()
                .modifierUserId(user.getUserId())
                .modifierUsername(user.getUsername())
                .agentId(user.getAgentId())
                .modifierRoleName(activeRoleAss == null ? null : activeRoleAss.getRole().getRoleName())
                .modifierRoleId(activeRoleAss == null ? null : activeRoleAss.getRole().getRoleId())
                .modifierStrId(activeRoleAss == null ? null : activeRoleAss.getStructure().getStrId())
                .modifierStrName(activeRoleAss == null ? null : activeRoleAss.getStructure().getStrName())
                .modificationDate(LocalDateTime.now())
                .modifierAssId(activeRoleAss == null ? null : activeRoleAss.getAssignationId())
                .nom(agent == null ? null : agent.getNom())
                .prenom(agent == null ? null : agent.getPrenom())
                .matricule(agent == null ? null : agent.getMatricule())
                .build();
    }
}
