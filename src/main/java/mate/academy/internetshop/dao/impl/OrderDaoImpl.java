package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.GeneratorId;
import mate.academy.internetshop.model.Order;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order create(Order order) {
        Order tempOrder = order;
        tempOrder.setOrderId(GeneratorId.getNewOrderId());
        Storage.orders.add(tempOrder);
        return tempOrder;
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
        int orderPos = 0;
        for (Order o : Storage.orders) {
            if (o.getOrderId().equals(order.getOrderId())) {
                break;
            }
            orderPos++;
        }
        Storage.orders.set(orderPos, order);
        return order;
    }

    @Override
    public List<Order> getAll() {
        return Storage.orders;
    }

    @Override
    public boolean deleteById(Long orderId) {
        Optional optionalOrder = Optional.ofNullable(Storage.orders
                .stream()
                .filter(i -> i.getOrderId().equals(orderId))
                .findFirst());
        if (optionalOrder.isEmpty()) {
            return false;
        }
        Storage.orders.remove(optionalOrder.get());
        return true;
    }

    @Override
    public boolean delete(Order order) {
        return Storage.orders.remove(order);
    }
}
