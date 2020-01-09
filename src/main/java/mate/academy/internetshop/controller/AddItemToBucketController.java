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
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.UserService;

public class AddItemToBucketController extends HttpServlet {
    private static final Long USER_ID = Long.valueOf(1);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static UserService userService;

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bucket bucket = bucketService.getAll().stream().filter( x -> x.getUserId().equals(USER_ID)).findFirst().get();

        List<Item> itemList = bucketService.getAllItems(bucket);
        req.setAttribute("bucketItems", itemList);

        String itemId = req.getParameter("item_id");
        Item item = itemService.get(Long.valueOf(itemId)).get();

        bucketService.addItem(bucket, item);
        req.getRequestDispatcher("WEB-INF/views/bucket.jsp").forward(req, resp);
    }
}
