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

public class GetAllItemsFromBucketController extends HttpServlet {
    private static final Long USER_ID = Long.valueOf(1);

    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bucket bucket = bucketService.getAll().stream().filter(x -> x.getUserId().equals(USER_ID)).findFirst().get();

        List<Item> itemList = bucketService.getAllItems(bucket);
        req.setAttribute("bucketItems", itemList);

        req.getRequestDispatcher("WEB-INF/views/bucket.jsp").forward(req, resp);
    }
}
