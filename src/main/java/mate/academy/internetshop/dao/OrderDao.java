package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Order;

public interface OrderDao {
    Order create(Order order);

    Optional<Order> get(Long orderId);

    Order update(Order order);

    List<Order> getAll();

    boolean deleteById(Long orderId);

    boolean delete(Order order);
}
