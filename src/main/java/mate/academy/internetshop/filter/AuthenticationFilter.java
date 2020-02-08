package mate.academy.internetshop.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(AuthorizationFilter.class);
    private static final String COOKIE = "MATE";

    @Inject
    private static UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if (req.getCookies() == null) {
            processAuthenticated(req, resp);
            return;
        }

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(COOKIE)) {
                try {
                    Optional<User> user = userService.findByToken(cookie.getValue());
                    if (user.isPresent()) {
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                } catch (DataProcessingException e) {
                    LOGGER.warn("Can't authentificate user", e);
                }
            }
        }
        processAuthenticated(req, resp);
    }

    @Override
    public void destroy() {
    }

    private void processAuthenticated(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
