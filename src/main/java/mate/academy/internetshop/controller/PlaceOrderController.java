package mate.academy.internetshop.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;

public class PlaceOrderController extends HttpServlet {
    private static final Long USER_ID = Long.valueOf(1);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static UserService userService;

    @Inject
    private static OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Bucket bucket = bucketService.getAll().stream()
                .filter(x -> x.getUserId()
                        .equals(USER_ID))
                .findFirst()
                .get();
        User user = userService.get(USER_ID).get();

        Order order = orderService.completeOrder(bucket.getItems(), user);
        bucketService.delete(bucket);

        List<Item> itemList = orderService.get(order.getOrderId()).get().getItems();
        req.setAttribute("items", itemList);
        req.setAttribute("amount", order.getAmount());
        req.setAttribute("order_id", order.getOrderId());

        req.getRequestDispatcher("WEB-INF/views/order.jsp").forward(req, resp);
    }
}
