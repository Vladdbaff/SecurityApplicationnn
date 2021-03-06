package mvc_security.config.handler;

import mvc_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    private final UserDetailsService detailsService;

    public LoginSuccessHandler(UserDetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            long id = ((User) (detailsService.loadUserByUsername(authentication.getName()))).getId();
            httpServletResponse.sendRedirect("/users/" + id);
        } else {
            httpServletResponse.sendRedirect("/login");
        }

    }
}
