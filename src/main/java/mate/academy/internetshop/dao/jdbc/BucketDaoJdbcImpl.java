package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Dao
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static final Logger LOGGER = LogManager.getLogger(ItemDaoJdbcImpl.class);
    private static String BUCKET_TABLE = "bucket";
    private static String BUCKET_ITEMS_TABLE = "bucket_item";
    private static String ITEMS_TABLE = "items";

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket create(Bucket entity) {
        Bucket bucket = entity;
        String query = String.format("INSERT INTO %s(user_id) VALUE(?)",
                BUCKET_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, entity.getUserId());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bucket.setBucketId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating bucket failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't create bucket", e);
        }

        for (Item item : bucket.getItems()) {
            String queryItems = String.format("INSERT INTO %s(bucket_id, item_id) VALUE(?, ?)",
                    BUCKET_ITEMS_TABLE);
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryItems)) {
                preparedStatement.setLong(1, bucket.getBucketId());
                preparedStatement.setLong(2, item.getItemId());
                int rows = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warn("Can't add item_id/bucket_id to bucket_item", e);
            }
        }
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        String query = String.format("SELECT bucket.bucket_id, bucket.user_id, "
                        + "items.item_id, items.name, items.price FROM %s "
                        + "LEFT JOIN %s ON bucket.bucket_id = bucket_item.bucket_id "
                        + "LEFT JOIN %s ON bucket_item.item_id = items.item_id "
                        + "WHERE bucket.bucket_id=?;", BUCKET_TABLE, BUCKET_ITEMS_TABLE,
                ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ResultSet resultSet = ps.executeQuery();
            Bucket bucket = null;
            List<Item> bucketItems = new ArrayList<>();
            while (resultSet.next()) {
                bucket = new Bucket(resultSet.getLong(2));
                bucket.setBucketId(resultSet.getLong(1));
                if (resultSet.getString(4) == null) {
                    continue;
                }
                Item item = new Item(resultSet.getString(4),
                        resultSet.getBigDecimal(5));
                item.setItemId(resultSet.getLong(3));
                bucketItems.add(item);
            }
            bucket.setItems(bucketItems);
            return Optional.of(bucket);
        } catch (SQLException e) {
            LOGGER.warn("Can't get bucket by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Bucket update(Bucket entity) {
        String query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_ITEMS_TABLE);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, entity.getBucketId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't update bucket", e);
        }

        query = String.format("INSERT INTO %s(bucket_id, item_id) VALUE(?, ?)",
                BUCKET_ITEMS_TABLE);
        for (Item item : entity.getItems()) {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, entity.getBucketId());
                ps.setLong(2, item.getItemId());
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                LOGGER.warn("Can't update bucket", e);
            }
        }
        return entity;
    }

    @Override
    public boolean deleteById(Long bucketId) {
        String query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_ITEMS_TABLE);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Can't delete bucket by ID", e);
        }

        query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.warn("Can't delete bucket by ID", e);
        }
        return false;
    }

    @Override
    public boolean delete(Bucket entity) {
        return deleteById(entity.getBucketId());
    }

    @Override
    public List<Bucket> getAll() {
        List<Bucket> tempBuckets = new ArrayList<>();
        String query = String.format("SELECT bucket_id, user_id FROM %s",
                BUCKET_TABLE);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long bucketId = resultSet.getLong(1);
                Long userId = resultSet.getLong(2);
                Bucket bucket = new Bucket(userId);
                bucket.setBucketId(bucketId);
                tempBuckets.add(bucket);
            }
        } catch (SQLException e) {
            LOGGER.warn("Can't get all buckets", e);
        }

        List<Bucket> buckets = new ArrayList<>();
        for (Bucket bucket : tempBuckets) {
            buckets.add(get(bucket.getBucketId()).get());
        }
        return buckets;
    }

    @Override
    public void clear(Bucket bucket) {
        String query = String.format("DELETE FROM %s WHERE bucket_id=%d",
                BUCKET_ITEMS_TABLE, bucket.getBucketId());

        try (Statement ps = connection.createStatement()) {
            ps.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.warn("Can't clear bucket", e);
        }
    }
}
