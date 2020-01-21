package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static final Logger LOGGER = LogManager.getLogger(ItemDaoJdbcImpl.class);
    private static String TABLE = "items";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item create(Item entity) {
        String query = "INSERT INTO " + TABLE + "(name, price) VALUES('"
                + entity.getName() + "', " + entity.getPrice() + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn("Can't create item" + entity.toString());
            LOGGER.warn("Exception was", e);
        }
        return entity;
    }

    @Override
    public Optional<Item> get(Long entityId) {
        String query = "SELECT * FROM " + TABLE + " WHERE item_id=" + entityId;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Item item = new Item(rs.getString("name"),
                        rs.getBigDecimal("price"));
                item.setItemId(rs.getLong("item_id"));
                return Optional.of(item);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get item bi ID" + entityId);
            LOGGER.warn("Exception was", e);
        }
        return Optional.empty();
    }

    @Override
    public Item update(Item entity) {
        String query = "UPDATE items SET name='" + entity.getName() + "', price="
                + entity.getPrice() + " WHERE item_id=" + entity.getItemId();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn("Can't update item" + entity.toString());
            LOGGER.warn("Exception was", e);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Long entityId) {
        Optional<Item> item = get(entityId);
        if (item.isPresent()) {
            String query = "DELETE FROM items WHERE item_id=" + entityId;
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                LOGGER.warn("Can't create item" + entityId);
                LOGGER.warn("Exception was", e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Item entity) {
        Optional<Item> item = get(entity.getItemId());
        if (item.isPresent()) {
            String query = "DELETE FROM items WHERE item_id=" + entity.getItemId();
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                LOGGER.warn("Can't create item" + entity.getItemId());
                LOGGER.warn("Exception was", e);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Item> getAll() {
        List<Item> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE;

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Item item = new Item(rs.getString("name"),
                        rs.getBigDecimal("price"));
                item.setItemId(rs.getLong("item_id"));
                list.add(item);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get items");
            LOGGER.warn("Exception was", e);
        }
        return list;
    }
}
