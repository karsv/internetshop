package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController extends HttpServlet {
    private static final String COOKIE = "MATE";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession().invalidate();

        Cookie cookie = new Cookie(COOKIE, null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        resp.sendRedirect(req.getContextPath() + "/index");

    }
}
