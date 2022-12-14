package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.entities.Agent;
import dgmp.sigrh.auth2.controller.repositories.MenuRepo;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.controller.services.IMenuService;
import dgmp.sigrh.auth2.model.entities.AppPrivilege;
import dgmp.sigrh.auth2.model.entities.AppRole;
import dgmp.sigrh.auth2.model.entities.AppUser;
import dgmp.sigrh.auth2.model.entities.PrincipalAss;
import dgmp.sigrh.auth2.model.events.EventActorIdentifier;
import dgmp.sigrh.auth2.security.authentication.token.AppUsernamePasswordAuthToken;
import dgmp.sigrh.instancemodule.controller.repositories.InstanceRepo;
import dgmp.sigrh.instancemodule.model.entities.Instance;
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
    private final AgentRepo agentRepo;
    private final InstanceRepo instanceRepo;
    private final MenuRepo menuRepo;
    private final IMenuService menuService;
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
    public boolean isAuthenticated()
    {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
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
    public boolean hasMenu(String menuCode)
    {
        return this.getAuthorities().stream().anyMatch(auth->
        {
            String[] prvCodes = menuService.getPrvCodesByMenuCode(menuCode);
            return Arrays.stream(prvCodes).anyMatch(prv->auth.equals(prv));
        });
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
    public Long getAuthAgentId() {
        return agentRepo.getAgtIdByUserId(getAuthUserId());
    }

    @Override
    public String getAuthAgentPhotoLink() {
        Long agentId = getAuthAgentId();
        return "/sigrh/agents/displayPhoto/" + (agentId == null ? -10 : agentId);
    }

    @Override
    public Instance getAuthInstance()
    {
        Long visibility = this.getVisibilityId();
        return visibility == null ? null : instanceRepo.getStrInstance(visibility);
    }

    @Override
    public String getAuthInstanceSigle()
    {
        Instance instance = this.getAuthInstance();
        return instance == null ? "" : instance.getHead() == null ? "" : "| " + instance.getHead().getStrSigle();
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
        Agent agent = user.getAgentId()==null ? null : agentRepo.findById(user.getAgentId()).orElse(null);
        return EventActorIdentifier.builder()
                .modifierUserId(user.getUserId())
                .modifierUsername(user.getUsername())
                .modifierAgentId(user.getAgentId())
                .modifierStrId(this.getVisibilityId())
                .modifierStrName(visibility == null ? null : visibility.getStrName())
                .modificationDate(LocalDateTime.now())
                .modifierAssId(principalAss == null ? null : principalAss.getAssId())
                .modifierNom(agent == null ? null : agent.getNom())
                .modifierPrenom(agent == null ? null : agent.getPrenom())
                .modifierMatricule(agent == null ? null : agent.getMatricule())
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
