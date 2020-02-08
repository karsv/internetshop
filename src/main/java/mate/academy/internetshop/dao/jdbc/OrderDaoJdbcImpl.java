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
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static final String ORDER_TABLE = "orders";
    private static final String ORDER_ITEMS_TABLE = "order_items";
    private static final String ITEMS_TABLE = "items";

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order create(Order order) throws DataProcessingException {
        Order newOrder = order;
        String query = String.format("INSERT INTO %s (user_id, amount) VALUES(?, ?)", ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, newOrder.getUserId());
            ps.setBigDecimal(2, newOrder.getAmount());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newOrder.setOrderId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create order", e);
        }

        insertIntoOrderItemTable(order);

        return newOrder;
    }

    @Override
    public Optional<Order> get(Long orderId) throws DataProcessingException {
        String query = String.format("SELECT order_id, user_id, amount "
                + "FROM %s WHERE order_id=(?);", ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getLong(1));
                order.setUserId(resultSet.getLong(2));
                order.setAmount(resultSet.getBigDecimal(3));
                order.setItems(getAllItemFromOrder(orderId));
                return Optional.of(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get order by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Order update(Order order) throws DataProcessingException {
        deleteOrderFromTableById(ORDER_ITEMS_TABLE, order.getOrderId());

        insertIntoOrderItemTable(order);

        String query = String.format("UPDATE %s SET amount=(?) WHERE order_id=?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBigDecimal(1, order.getAmount());
            ps.setLong(2, order.getOrderId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update order", e);
        }
        return order;
    }

    private void insertIntoOrderItemTable(Order order) throws DataProcessingException {
        String query = String.format("INSERT INTO %s(order_id, item_id) VALUE(?, ?)",
                ORDER_ITEMS_TABLE);
        for (Item item : order.getItems()) {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, order.getOrderId());
                ps.setLong(2, item.getItemId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException(
                        "Can't insert new items to order_items", e);
            }
        }
    }

    @Override
    public void deleteById(Long orderId) throws DataProcessingException {
        deleteOrderFromTableById(ORDER_ITEMS_TABLE, orderId);
        deleteOrderFromTableById(ORDER_TABLE, orderId);
    }

    private void deleteOrderFromTableById(String table, Long orderId)
            throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE order_id =?",
                table);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete order from" + table, e);
        }
    }

    @Override
    public void delete(Order order) throws DataProcessingException {
        deleteById(order.getOrderId());
    }

    @Override
    public List<Order> getAll() throws DataProcessingException {
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
                order.setItems(getAllItemFromOrder(orderId));
                tempOrders.add(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all orders", e);
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : tempOrders) {
            orders.add(get(order.getOrderId()).get());
        }
        return orders;
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) throws DataProcessingException {
        List<Order> tempOrders = new ArrayList<>();
        String query = String.format("SELECT order_id, amount FROM %s WHERE user_id=?",
                ORDER_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long orderId = resultSet.getLong(1);
                BigDecimal amount = resultSet.getBigDecimal(2);
                Order order = new Order();
                order.setOrderId(orderId);
                order.setUserId(userId);
                order.setAmount(amount);
                order.setItems(getAllItemFromOrder(orderId));
                tempOrders.add(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all orders", e);
        }

        List<Order> orders = new ArrayList<>();
        for (Order order : tempOrders) {
            orders.add(get(order.getOrderId()).get());
        }
        return orders;
    }

    private List<Item> getAllItemFromOrder(Long orderId) throws DataProcessingException {
        List<Item> listOfItems = new ArrayList<>();
        String query = String.format("SELECT items.item_id, name, price FROM %s items JOIN %s oit"
                        + " ON items.item_id = oit.item_id AND order_id = ?",
                ITEMS_TABLE, ORDER_ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long itemId = rs.getLong(1);
                String itemName = rs.getString(2);
                BigDecimal itemPrice = rs.getBigDecimal(3);
                Item item = new Item(itemName, itemPrice);
                item.setItemId(itemId);
                listOfItems.add(item);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all items from order", e);
        }
        return listOfItems;
    }
}
