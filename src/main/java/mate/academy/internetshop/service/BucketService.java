package mate.academy.internetshop.service;

import java.util.List;

import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

public interface BucketService extends GenericService<Bucket, Long> {
    void addItem(Bucket bucket, Item item) throws JdbcDaoException;

    void deleteItem(Bucket bucket, Item item) throws JdbcDaoException;

    void clear(Bucket bucket) throws JdbcDaoException;

    Bucket getByUserId(Long userId) throws JdbcDaoException;

    List<Item> getAllItems(Bucket bucket) throws JdbcDaoException;
}
