package mate.academy.internetshop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.User;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Item item1 = new Item("Item1", BigDecimal.valueOf(1.0));
        Item item2 = new Item("Item2", BigDecimal.valueOf(2.0));
        Item item3 = new Item("Item3", BigDecimal.valueOf(3.0));
        Item item4 = new Item("Item4", BigDecimal.valueOf(4.0));
        Item item5 = new Item("Item5", BigDecimal.valueOf(5.0));
        itemService.create(item1);
        itemService.create(item2);
        itemService.create(item3);
        itemService.create(item4);
        itemService.create(item5);

        User user = new User("User 1", "123");
        User user1 = new User("User 2", "123");
        User user2 = new User("User 3", "123");

        userService.create(user);
        userService.create(user1);
        userService.create(user2);

        resp.sendRedirect(req.getContextPath() + "/index");
    }
}
