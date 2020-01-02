package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Order;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order create(Order order) {
        Storage.orders.add(order);
        return order;
    }

    @Override
    public Optional<Order> get(Long orderId) {
        return Optional.ofNullable(Storage.orders
                .stream()
                .filter(b -> b.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(()
                        -> new NoSuchElementException("Can't find order with id: "
                        + orderId)));
    }

    @Override
    public Order update(Order order) {
        Optional oldOrder = Optional.ofNullable(Storage.orders.stream()
                .filter(i -> i.getOrderId().equals(order.getOrderId()))
                .findFirst());
        Storage.orders.remove(oldOrder.get());
        Storage.orders.add(order);
        return order;
    }

    @Override
    public List<Order> getAll() {
        return Storage.orders;
    }

    @Override
    public boolean delete(Long orderId) {
        Optional optionalOrder = Optional.ofNullable(Storage.orders
                .stream()
                .filter(i -> i.getOrderId().equals(orderId))
                .findFirst())
                .orElseThrow(() -> new NoSuchElementException("Can't find order with id: "
                        + orderId));
        Storage.orders.remove(optionalOrder.get());
        return true;
    }

    @Override
    public boolean delete(Order order) {
        if (!Storage.orders.remove(order)) {
            throw new NoSuchElementException("Can't find order with id: "
                    + order.getOrderId());
        }
        return true;
    }
}
