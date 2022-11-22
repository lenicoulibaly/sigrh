package dgmp.sigrh.auth2.security.handlers;

import dgmp.sigrh.auth2.security.services.ISecurityContextManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component @RequiredArgsConstructor
public class AppSuccessfullAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler
{
    private final ISecurityContextManager scm;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect(this.determineTargetUrl(request, response, authentication));
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        return "/sigrh/assignations/user-ass-list?userId=" + scm.getAuthUserId();
    }
}
