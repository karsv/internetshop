package mate.academy.internetshop.dao;

import java.util.List;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Order;

public interface OrderDao extends GenericDao<Order, Long> {
    List<Order> getAllOrdersByUserId(Long userId) throws DataProcessingException;
}
