package dgmp.sigrh.auth2.security.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
    {
        System.out.println(exception.getClass());
        //BadCredentialsException username ou mot de passe incorrect
        //org.springframework.security.authentication.LockedException Votre compte a été suspendu par un administrateur
        String errMsg = exception instanceof DisabledException ? "Votre compte a bien été créé mais n'est pas encore activé.\nPour recevoir un lien d'activation, veillez cliquer sur le lien ci-dessous." :exception instanceof LockedException ? "Compte bloqué" : "Username ou mot de passe incorrect";
        response.sendRedirect("/login?errMsg=" + Base64.getUrlEncoder().encodeToString(errMsg.getBytes()));
    }
}
