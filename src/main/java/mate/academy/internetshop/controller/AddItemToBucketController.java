package mate.academy.internetshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddItemToBucketController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AddItemToBucketController.class);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long userId = (Long) req.getSession().getAttribute("userId");
            Bucket bucket = null;
            bucket = bucketService.getByUserId(userId);
            String itemId = req.getParameter("item_id");
            Item item = itemService.get(Long.valueOf(itemId)).get();
            bucketService.addItem(bucket, item);
        } catch (JdbcDaoException e) {
            LOGGER.warn("Can't add item to bucket", e);
        }

        resp.sendRedirect(req.getContextPath() + "/servlet/items");
    }
}
