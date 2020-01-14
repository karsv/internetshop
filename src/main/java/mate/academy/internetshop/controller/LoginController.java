package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

public class LoginController extends HttpServlet {
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try {
            User user = userService.login(login, password);
            Cookie cookie = new Cookie("MATE", user.getToken());
            resp.addCookie(cookie);
            HttpSession httpSession = req.getSession(true);
            httpSession.setAttribute("userId", user.getUserId());
            resp.sendRedirect(req.getContextPath() + "/index");
        } catch (AuthentificationException e) {
            req.setAttribute("errorMsg", "Wrong parameters!");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
