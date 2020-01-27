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

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

@Dao
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static String BUCKET_TABLE = "bucket";
    private static String BUCKET_ITEMS_TABLE = "bucket_item";
    private static String ITEMS_TABLE = "items";

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket create(Bucket entity) throws DataProcessingException {
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
            throw new DataProcessingException("Can't create bucket", e);
        }

        insertIntoBucketItemTable(bucket);
        return bucket;
    }

    private void insertIntoBucketItemTable(Bucket bucket) throws DataProcessingException {
        for (Item item : bucket.getItems()) {
            String queryItems = String.format("INSERT INTO %s(bucket_id, item_id) VALUE(?, ?)",
                    BUCKET_ITEMS_TABLE);
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryItems)) {
                preparedStatement.setLong(1, bucket.getBucketId());
                preparedStatement.setLong(2, item.getItemId());
                int rows = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't add item_id/bucket_id to bucket_item", e);
            }
        }
    }

    @Override
    public Optional<Bucket> get(Long bucketId) throws DataProcessingException {
        String query = String.format("SELECT bucket_id, user_id FROM %s WHERE bucket_id=?;",
                BUCKET_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ResultSet resultSet = ps.executeQuery();
            Bucket bucket = null;
            if (resultSet.next()) {
                bucket = new Bucket(resultSet.getLong(2));
                bucket.setBucketId(resultSet.getLong(1));
                bucket.setItems(getAllItemFromBucket(bucket.getBucketId()));
                return Optional.of(bucket);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get bucket by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public Bucket update(Bucket bucket) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_ITEMS_TABLE);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, bucket.getBucketId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update bucket, "
                    + "due delete items by bucket_id in bucket_items", e);
        }

        insertIntoBucketItemTable(bucket);
        return bucket;
    }

    @Override
    public boolean deleteById(Long bucketId) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_ITEMS_TABLE);

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete items at bucket_item by bucket_id", e);
        }

        query = String.format("DELETE FROM %s WHERE bucket_id=?",
                BUCKET_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete bucket bu bucket_id", e);
        }
    }

    @Override
    public boolean delete(Bucket entity) throws DataProcessingException {
        return deleteById(entity.getBucketId());
    }

    @Override
    public List<Bucket> getAll() throws DataProcessingException {
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
                bucket.setItems(getAllItemFromBucket(bucketId));
                tempBuckets.add(bucket);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all buckets", e);
        }

        return tempBuckets;
    }

    private List<Item> getAllItemFromBucket(Long bucketId) throws DataProcessingException {
        List<Item> listOfItems = new ArrayList<>();
        String query = String.format("SELECT items.item_id, name, price FROM %s items JOIN %s bit"
                        + " ON items.item_id = bit.item_id AND bucket_id = ?",
                ITEMS_TABLE, BUCKET_ITEMS_TABLE);
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, bucketId);
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
            throw new DataProcessingException("Can't get all items from bucket", e);
        }
        return listOfItems;
    }

    @Override
    public void clear(Bucket bucket) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE bucket_id=%d",
                BUCKET_ITEMS_TABLE, bucket.getBucketId());

        try (Statement ps = connection.createStatement()) {
            ps.executeUpdate(query);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't clear bucket", e);
        }
    }
}
