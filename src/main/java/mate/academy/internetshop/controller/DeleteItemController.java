package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.exceptions.DataProcessingException;
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
        } catch (DataProcessingException e) {
            LOGGER.error("Can't delete item by Id", e);
            req.setAttribute("errorMsg", "Error due delete item!");
            req.getRequestDispatcher("/WEB-INF/views/processExc.jsp").forward(req, resp);
        }
        resp.sendRedirect(req.getContextPath() + "/servlet/items");
    }
}
