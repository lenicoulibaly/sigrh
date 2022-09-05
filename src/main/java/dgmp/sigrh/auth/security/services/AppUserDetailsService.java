package dgmp.sigrh.auth.security.services;

import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService
{
    private final UserDAO userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser loadedUser =userDao.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username introuvable"));
        return new AppUserDetails(loadedUser);//User.withUserDetails(new AppUserDetails(loadedUser)).build();
    }
}
