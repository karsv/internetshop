package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteItemController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DeleteItemController.class);

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            itemService.deleteById(Long.valueOf(req.getParameter("item_id")));
        } catch (JdbcDaoException e) {
            LOGGER.warn("Can't delete item by Id", e);
        }
        resp.sendRedirect(req.getContextPath() + "/servlet/items");
    }
}
