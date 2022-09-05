package dgmp.sigrh.auth.security.authentication.token;

import dgmp.sigrh.auth.security.services.AppUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AppUsernamePasswordAuthToken extends UsernamePasswordAuthenticationToken
{

    public AppUsernamePasswordAuthToken(String username, String password, Collection<? extends GrantedAuthority> authorities, AppUserDetails userDetails) {
        super(username, password, authorities);
        setDetails(userDetails);
    }

    public AppUsernamePasswordAuthToken(AppUserDetails details, String password, Collection<? extends GrantedAuthority> authorities) {
        super(details, password, authorities);
        setDetails(details);
    }

    public AppUsernamePasswordAuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AppUsernamePasswordAuthToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
