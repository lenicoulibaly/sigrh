package dgmp.sigrh.auth2.security.api;

import dgmp.sigrh.auth2.security.services.IAuthoritiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/staffadmin/auth-server/authorities")
public class AuthoritiesAPI
{
    private final IAuthoritiesService authoritiesService;

    @GetMapping(path = "")
    //@PreAuthorize("hasAuthority('ADMIN')")
    @PreAuthorize("permitAll()")
    public Set<String> getUserAuthorities(@RequestParam long userId)
    {
        return authoritiesService.getUserAuthorities(userId);
    }
}
