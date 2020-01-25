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
import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static String ORDER_TABLE = "orders";
    private static String ORDER_ITEMS_TABLE = "order_items";
    private static String ITEMS_TABLE = "items";

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order create(Order order) throws JdbcDaoException {
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
            throw new JdbcDaoException("Can't create order");
        }

        for (Item item : newOrder.getItems()) {
            query = String.format("INSERT INTO %s (order_id, item_id) VALUES(?, ?)",
                    ORDER_ITEMS_TABLE);
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, newOrder.getOrderId());
                ps.setLong(2, item.getItemId());
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                throw new JdbcDaoException("Can't add items by order_id to order_items");
            }
        }

        return newOrder;
    }

    @Override
    public Optional<Order> get(Long orderId) throws JdbcDaoException {
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
            throw new JdbcDaoException("Can't get order by id");
        }
    }

    @Override
    public Order update(Order order) throws JdbcDaoException {
        String query = String.format("DELETE FROM %s WHERE order_id=?",
                ORDER_ITEMS_TABLE);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, order.getOrderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcDaoException("Can't delete orders items due update from order_items");
        }

        query = String.format("INSERT INTO %s(order_id, item_id) VALUE(?, ?)",
                ORDER_ITEMS_TABLE);
        for (Item item : order.getItems()) {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, order.getOrderId());
                ps.setLong(2, item.getItemId());
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                throw new JdbcDaoException("Can't insert new items to order_items due update");
            }
        }

        query = String.format("UPDATE %s SET amount=(?) WHERE order_id=?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBigDecimal(1, order.getAmount());
            ps.setLong(2, order.getOrderId());
            int rows = ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcDaoException("Can't update order");
        }
        return order;
    }

    @Override
    public boolean deleteById(Long orderId) throws JdbcDaoException {
        String query = String.format("DELETE FROM %s WHERE order_id =?",
                ORDER_ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            int rows = ps.executeUpdate();
        } catch (SQLException e) {
            throw new JdbcDaoException("Can't delete orders items due delete "
                    + "Order from order_items");
        }

        query = String.format("DELETE FROM %s WHERE order_id =?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            int rows = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new JdbcDaoException("Can't delete order");
        }
    }

    @Override
    public boolean delete(Order order) throws JdbcDaoException {
        return deleteById(order.getOrderId());
    }

    @Override
    public List<Order> getAll() throws JdbcDaoException {
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
            throw new JdbcDaoException("Can't get all orders");
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : tempOrders) {
            orders.add(get(order.getOrderId()).get());
        }
        return orders;
    }
}
