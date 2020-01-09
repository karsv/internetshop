package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;

public class DeleteItemFromBucketController extends HttpServlet {
    private static final Long USER_ID = Long.valueOf(1);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bucket bucket = bucketService.getAll().stream().filter(x -> x.getUserId().equals(USER_ID)).findFirst().get();

        Long itemId = Long.valueOf(req.getParameter("item_id"));
        Item item = itemService.get(itemId).get();

        bucketService.deleteItem(bucket, item);
        resp.sendRedirect(req.getContextPath() + "/bucket");
    }
}
