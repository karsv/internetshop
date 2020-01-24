package mate.academy.internetshop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.UserService;

public class InjectDataController extends HttpServlet {
    private static final Long USER_ID = 1L;

    @Inject
    private static ItemService itemService;

    @Inject
    private static UserService userService;

    @Inject
    private static BucketService bucketService;

    @Inject
    private static BucketDao bucketDao;

    @Inject
    private static OrderDao orderDao;

    @Inject
    private static ItemDao itemDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Item> list = new ArrayList<>();
        list.add(itemDao.get(1L).get());
        list.add(itemDao.get(4L).get());
        Order order = new Order(list, 14L);
        Order newOrder = orderDao.get(3L).get();
        newOrder.setAmount(BigDecimal.valueOf(5));
        newOrder.setItems(list);
        List<Order> listO = orderDao.getAll();
        resp.sendRedirect(req.getContextPath() + "/index");
    }
}
