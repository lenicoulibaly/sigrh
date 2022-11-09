package dgmp.sigrh.auth2.security.services;

import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import dgmp.sigrh.auth2.model.entities.AppUser;
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
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser loadedUser =userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username introuvable"));
        return new AppUserDetails(loadedUser);//User.withUserDetails(new AppUserDetails(loadedUser)).build();
    }
}
