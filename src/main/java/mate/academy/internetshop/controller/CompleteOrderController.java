package mate.academy.internetshop.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompleteOrderController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(CompleteOrderController.class);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static UserService userService;

    @Inject
    private static OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Long userId = (Long) req.getSession().getAttribute("userId");
            Bucket bucket = bucketService.getByUserId(userId);
            User user = null;
            user = userService.get(userId).get();
            Order order = orderService.completeOrder(bucket.getItems(), user);
            bucketService.clear(bucket);
            List<Item> itemList = null;
            itemList = orderService.get(order.getOrderId()).get().getItems();
            req.setAttribute("items", itemList);
            req.setAttribute("amount", order.getAmount());
            req.setAttribute("order_id", order.getOrderId());
        } catch (JdbcDaoException e) {
            LOGGER.warn("Can't complete order", e);
        }

        req.getRequestDispatcher("/WEB-INF/views/order.jsp").forward(req, resp);
    }
}
