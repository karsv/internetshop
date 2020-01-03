package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;

public interface OrderService {
    Order create(Order order);

    Optional<Order> get(Long orderId);

    Order update(Order order);

    boolean delete(Long orderId);

    boolean delete(Order order);

    Order completeOrder(List<Item> items, User user);

    Optional<List<Order>> getUserOrders(User user);
}
