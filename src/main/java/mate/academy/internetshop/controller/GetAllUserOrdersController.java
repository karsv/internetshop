package mate.academy.internetshop.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetAllUserOrdersController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(GetAllUserOrdersController.class);

    @Inject
    private static OrderService orderService;

    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long userId = (Long) req.getSession().getAttribute("userId");
            User user = null;
            user = userService.get(userId).get();
            List<Order> orderList = orderService.getUserOrders(user);
            req.setAttribute("orders", orderList);
        } catch (DataProcessingException e) {
            LOGGER.error("Can't get all users orders", e);
            req.setAttribute("errorMsg", "Error due get all user orders!");
            req.getRequestDispatcher("/WEB-INF/views/processExc.jsp").forward(req, resp);
        }
        req.getRequestDispatcher("/WEB-INF/views/userOrders.jsp").forward(req, resp);
    }
}
