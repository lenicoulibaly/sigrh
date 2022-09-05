package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.SpringContext;
import dgmp.sigrh.auth.controller.repositories.PrivilegeToRoleAssRepo;
import dgmp.sigrh.auth.controller.repositories.PrivilegeToUserAssRepo;
import dgmp.sigrh.auth.model.entities.AppUser;
import dgmp.sigrh.auth.model.entities.PrivilegeToRoleAss;
import dgmp.sigrh.auth.model.entities.PrivilegeToUserAss;
import dgmp.sigrh.auth.model.entities.RoleToUserAss;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class AppUserDetails implements UserDetails
{
    private AppUser appUser;

    private IAuthoritiesService authoritiesService = SpringContext.getBean(IAuthoritiesService.class);
    private PrivilegeToRoleAssRepo ptrAssRepo = SpringContext.getBean(PrivilegeToRoleAssRepo.class);
    private PrivilegeToUserAssRepo ptuAssRepo = SpringContext.getBean(PrivilegeToUserAssRepo.class);

    private RoleToUserAss  activeRoleAss ;
    private Set<PrivilegeToRoleAss> activeRolesPrvAsses;
    private Set<PrivilegeToUserAss> usersDirectPrivilegeAsses;

    public AppUserDetails(AppUser appUser)
    {
        this.appUser = appUser;
        activeRoleAss = this.authoritiesService.getActiveRoleAss(appUser.getUserId());
        activeRolesPrvAsses = activeRoleAss == null ? new HashSet<>() : ptrAssRepo.getPrivilegeAssForRole(activeRoleAss.getRole().getRoleId());
        usersDirectPrivilegeAsses = activeRoleAss == null ? new HashSet<>() : ptuAssRepo.getUsersDirectPrivilegeAss(appUser.getUserId(), activeRoleAss.getStructure().getStrId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return appUser==null ? new HashSet<>() : this.authoritiesService.getAuthorities(appUser.getUserId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() { return appUser.getPassword();}

    @Override
    public String getUsername() { return appUser.getUsername(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return appUser.isNotBlocked();}

    @Override
    public boolean isCredentialsNonExpired() { return true;}

    @Override
    public boolean isEnabled() { return appUser.isActive();}

    @Override
    public String toString() {
        return "AppUserDetails{" +
                "appUser=" + appUser.toString() +
                '}';
    }
}
