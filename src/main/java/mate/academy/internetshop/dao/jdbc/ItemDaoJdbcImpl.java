package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static String ITEMS_TABLE = "items";
    private static String BUCKET_ITEM_TABLE = "bucket_item";
    private static String ORDER_ITEM_TABLE = "order_items";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item create(Item entity) throws DataProcessingException {
        String query = String.format("INSERT INTO %s(name, price) VALUES(?, ?)",
                ITEMS_TABLE, entity.getName(), entity.getPrice());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create item", e);
        }
        return entity;
    }

    @Override
    public Optional<Item> get(Long itemId) throws DataProcessingException {
        String query = String.format("SELECT * FROM %s WHERE item_id=?", ITEMS_TABLE);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, itemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getString("name"),
                        rs.getBigDecimal("price"));
                item.setItemId(rs.getLong("item_id"));
                return Optional.of(item);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't item bu id", e);
        }
        return Optional.empty();
    }

    @Override
    public Item update(Item entity) throws DataProcessingException {
        String query = "UPDATE items SET name=?, price=? WHERE item_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.setLong(3, entity.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update item", e);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Long longId) throws DataProcessingException {
        Optional<Item> item = get(longId);
        if (item.isPresent()) {
            String query = String.format("DELETE FROM %s WHERE item_id=?", BUCKET_ITEM_TABLE);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, longId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't delete item by id in bucket_item", e);
            }

            query = String.format("DELETE FROM %s WHERE item_id=?", ORDER_ITEM_TABLE);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, longId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't delete item by id in order_item", e);
            }

            query = String.format("DELETE FROM %s WHERE item_id=?", ITEMS_TABLE);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, longId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't delete item by id", e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Item entity) throws DataProcessingException {
        return deleteById(entity.getItemId());
    }

    @Override
    public List<Item> getAll() throws DataProcessingException {
        List<Item> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", ITEMS_TABLE);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getString("name"),
                        rs.getBigDecimal("price"));
                item.setItemId(rs.getLong("item_id"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all items", e);
        }
        return list;
    }
}
