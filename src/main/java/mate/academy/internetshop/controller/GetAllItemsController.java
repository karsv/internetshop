package mate.academy.internetshop.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetAllItemsController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(GetAllItemsController.class);

    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Item> list = null;
        try {
            list = itemService.getAll();
        } catch (DataProcessingException e) {
            LOGGER.warn("Can't get all items", e);
        }
        req.setAttribute("items", list);
        req.getRequestDispatcher("/WEB-INF/views/items.jsp").forward(req, resp);
    }
}
