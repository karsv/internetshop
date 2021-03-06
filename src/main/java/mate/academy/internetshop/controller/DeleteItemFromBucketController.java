package mate.academy.internetshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteItemFromBucketController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteItemFromBucketController.class);

    @Inject
    private static BucketService bucketService;

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long userId = (Long) req.getSession().getAttribute("userId");
            Bucket bucket = bucketService.getByUserId(userId);
            Long itemId = Long.valueOf(req.getParameter("item_id"));
            Item item = itemService.get(itemId).get();
            bucketService.deleteItem(bucket, item);
        } catch (DataProcessingException e) {
            LOGGER.error("Can't delete item from bucket", e);
            req.setAttribute("errorMsg", "Error due delete item to bucket!");
            req.getRequestDispatcher("/WEB-INF/views/processExc.jsp").forward(req, resp);
        }

        resp.sendRedirect(req.getContextPath() + "/servlet/bucket");
    }
}
