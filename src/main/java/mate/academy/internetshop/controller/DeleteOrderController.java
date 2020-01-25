package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteOrderController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteOrderController.class);

    @Inject
    private static OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            orderService.deleteById(Long.valueOf(req.getParameter("order_id")));
        } catch (JdbcDaoException e) {
            LOGGER.warn("Can't delete order", e);
        }
        resp.sendRedirect(req.getContextPath() + "/servlet/userOrders");
    }
}
