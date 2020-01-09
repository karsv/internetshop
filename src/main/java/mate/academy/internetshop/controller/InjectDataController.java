package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.UserService;

public class InjectDataController extends HttpServlet {
    @Inject
    private static ItemService itemService;

    @Inject
    private static UserService userService;

    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Item item1 = new Item("Item1", 1.0);
        Item item2 = new Item("Item2", 2.0);
        Item item3 = new Item("Item3", 3.0);
        Item item4 = new Item("Item4", 4.0);
        Item item5 = new Item("Item5", 5.0);
        itemService.create(item1);
        itemService.create(item2);
        itemService.create(item3);
        itemService.create(item4);
        itemService.create(item5);

        User user = new User("User");
        userService.create(user);

        Bucket bucket = new Bucket();
        bucket.setUserId(user.getUserId());
        bucketService.create(bucket);
        bucketService.addItem(bucket, item1);
        bucketService.addItem(bucket, item3);
        bucketService.addItem(bucket, item5);

        resp.sendRedirect(req.getContextPath() + "/items");
    }
}