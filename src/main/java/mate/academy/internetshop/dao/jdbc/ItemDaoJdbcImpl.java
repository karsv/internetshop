package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = String.format("INSERT INTO %s(name, price) VALUES(?, ?)",
                TABLE, entity.getName(), entity.getPrice());

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't create item" + entity.toString(), e);
        }
        return entity;
    }

    @Override
    public Optional<Item> get(Long itemId) {
        String query = String.format("SELECT * FROM %s WHERE item_id=?", TABLE);

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
            LOGGER.warn("Can't get item bi ID" + itemId, e);
        }
        return Optional.empty();
    }

    @Override
    public Item update(Item entity) {
        String query = "UPDATE items SET name=?, price=? WHERE item_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.setLong(3, entity.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't update item" + entity.toString(), e);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Long longId) {
        Optional<Item> item = get(longId);
        if (item.isPresent()) {
            String query = String.format("DELETE FROM items WHERE item_id=?", longId);
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, longId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warn("Can't delete item by ID" + longId, e);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Item entity) {
        deleteById(entity.getItemId());
        return false;
    }

    @Override
    public List<Item> getAll() {
        List<Item> list = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Item item = new Item(rs.getString("name"),
                        rs.getBigDecimal("price"));
                item.setItemId(rs.getLong("item_id"));
                list.add(item);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get items", e);
        }
        return list;
    }
}
