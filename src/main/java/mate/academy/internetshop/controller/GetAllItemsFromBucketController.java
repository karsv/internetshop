package mate.academy.internetshop.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.service.BucketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetAllItemsFromBucketController extends HttpServlet {
    private static final Logger LOGGER =
            LogManager.getLogger(GetAllItemsFromBucketController.class);

    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Long userId = (Long) req.getSession().getAttribute("userId");
            Bucket bucket = bucketService.getByUserId(userId);
            bucketService.update(bucket);
            req.setAttribute("bucket", bucket);
        } catch (DataProcessingException e) {
            LOGGER.error("Can't get all items from bucket", e);
            req.setAttribute("errorMsg", "Error due get all items from bucket!");
            req.getRequestDispatcher("/WEB-INF/views/processExc.jsp").forward(req, resp);
        }

        req.getRequestDispatcher("/WEB-INF/views/bucket.jsp").forward(req, resp);
    }
}
