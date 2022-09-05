package dgmp.sigrh.auth.security.filters;

import dgmp.sigrh.auth.controller.repositories.UserDAO;
import dgmp.sigrh.auth.security.authentication.token.AppUsernamePasswordAuthToken;
import dgmp.sigrh.auth.security.services.AppUserDetails;
import dgmp.sigrh.auth.security.services.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component @RequiredArgsConstructor
public class ContextFilter extends OncePerRequestFilter
{
    private final AppUserDetailsService userDetailsService;
    private final UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        if(SecurityContextHolder.getContext().getAuthentication() == null)
        {
            chain.doFilter(request, response);
            return;
        }
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
        {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if(!userDAO.alreadyExistsByUsername(username))
            {
                chain.doFilter(request, response);
                return;
            }
            AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(username);
            AppUsernamePasswordAuthToken token = new AppUsernamePasswordAuthToken(username, null, SecurityContextHolder.getContext().getAuthentication().getAuthorities(), userDetails);
            SecurityContextHolder.getContext().setAuthentication(token);
            chain.doFilter(request, response);
        }
    }
}
