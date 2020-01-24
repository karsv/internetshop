package mate.academy.internetshop.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static final Logger LOGGER = LogManager.getLogger(OrderDaoJdbcImpl.class);
    private static String ORDER_TABLE = "orders";
    private static String ORDER_ITEMS_TABLE = "order_items";
    private static String ITEMS_TABLE = "items";

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order create(Order order) {
        Order newOrder = order;
        String query = String.format("INSERT INTO %s (user_id, amount) VALUES(?, ?)", ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, newOrder.getUserId());
            ps.setBigDecimal(2, newOrder.getAmount());
            int rows = ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newOrder.setOrderId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't create order", e);
        }

        for (Item item : newOrder.getItems()) {
            query = String.format("INSERT INTO %s (order_id, item_id) VALUES(?, ?)",
                    ORDER_ITEMS_TABLE);
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, newOrder.getOrderId());
                ps.setLong(2, item.getItemId());
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warn("Can't add items from order to order_item", e);
            }
        }

        return newOrder;
    }

    @Override
    public Optional<Order> get(Long orderId) {
        String query = String.format("SELECT orders.order_id, orders.user_id, orders.amount, "
                + "items.item_id, items.name, items.price "
                + "FROM %s LEFT JOIN %s ON orders.order_id = order_items.order_id "
                + "LEFT JOIN %s ON order_items.item_id = items.item_id "
                + "WHERE orders.order_id=(?);", ORDER_TABLE, ORDER_ITEMS_TABLE, ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            ResultSet resultSet = ps.executeQuery();
            Order order = new Order();
            List<Item> itemList = new ArrayList<>();
            while (resultSet.next()) {
                order.setOrderId(resultSet.getLong(1));
                order.setUserId(resultSet.getLong(2));
                order.setAmount(resultSet.getBigDecimal(3));
                Long itemId = resultSet.getLong(4);
                String itemName = resultSet.getString(5);
                BigDecimal itemPrice = resultSet.getBigDecimal(6);
                Item item = new Item(itemName, itemPrice);
                item.setItemId(itemId);
                itemList.add(item);
            }
            order.setItems(itemList);
            return Optional.of(order);
        } catch (SQLException e) {
            LOGGER.warn("Can't get order by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Order update(Order order) {
        String query = String.format("DELETE FROM %s WHERE order_id=?",
                ORDER_ITEMS_TABLE);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, order.getOrderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't update order 1", e);
        }

        query = String.format("INSERT INTO %s(order_id, item_id) VALUE(?, ?)",
                ORDER_ITEMS_TABLE);
        for (Item item : order.getItems()) {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, order.getOrderId());
                ps.setLong(2, item.getItemId());
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warn("Can't update order 2", e);
            }
        }

        query = String.format("UPDATE %s SET amount=(?) WHERE order_id=?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBigDecimal(1, order.getAmount());
            ps.setLong(2, order.getOrderId());
            int rows = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't update order 3", e);
        }
        return order;
    }

    @Override
    public boolean deleteById(Long orderId) {
        String query = String.format("DELETE FROM %s WHERE order_id =?",
                ORDER_ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            int rows = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("1 Can't delete order by ID", e);
        }

        query = String.format("DELETE FROM %s WHERE order_id =?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            int rows = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.warn("2 Can't delete order by ID", e);
        }
        return false;
    }

    @Override
    public boolean delete(Order order) {
        return deleteById(order.getOrderId());
    }

    @Override
    public List<Order> getAll() {
        List<Order> tempOrders = new ArrayList<>();
        String query = String.format("SELECT order_id, user_id, amount FROM %s",
                ORDER_TABLE);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long orderId = resultSet.getLong(1);
                Long userId = resultSet.getLong(2);
                BigDecimal amount = resultSet.getBigDecimal(3);
                Order order = new Order();
                order.setOrderId(orderId);
                order.setUserId(userId);
                order.setAmount(amount);
                tempOrders.add(order);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get all orders", e);
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : tempOrders) {
            orders.add(get(order.getOrderId()).get());
        }
        return orders;
    }
}
