package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.SpringContext;
import dgmp.sigrh.auth2.model.entities.AppUser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
@Setter
@Slf4j
public class AppUserDetails implements UserDetails
{
    private AppUser appUser;

    private IAuthoritiesService authoritiesService = SpringContext.getBean(IAuthoritiesService.class);


    public AppUserDetails(AppUser appUser)
    {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return appUser==null ? new HashSet<>() : this.authoritiesService.getUserAuthorities(appUser.getUserId()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
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
