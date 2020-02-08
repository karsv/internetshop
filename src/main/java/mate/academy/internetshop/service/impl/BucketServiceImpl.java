package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;

@Service
public class BucketServiceImpl implements BucketService {

    @Inject
    private static BucketDao bucketDao;

    @Override
    public Bucket create(Bucket bucket) throws DataProcessingException {
        return bucketDao.create(bucket);
    }

    @Override
    public Optional<Bucket> get(Long bucketId) throws DataProcessingException {
        return bucketDao.get(bucketId);
    }

    @Override
    public Bucket update(Bucket bucket) throws DataProcessingException {
        return bucketDao.update(bucket);
    }

    @Override
    public List<Bucket> getAll() throws DataProcessingException {
        return bucketDao.getAll();
    }

    @Override
    public void deleteById(Long bucketId) throws DataProcessingException {
        bucketDao.deleteById(bucketId);
    }

    @Override
    public void delete(Bucket bucket) throws DataProcessingException {
        bucketDao.delete(bucket);
    }

    @Override
    public void addItem(Bucket bucket, Item item) throws DataProcessingException {
        bucket.getItems().add(item);
        bucketDao.update(bucket);
    }

    @Override
    public void deleteItem(Bucket bucket, Item item) throws DataProcessingException {
        Bucket newBucket = bucketDao.get(bucket.getBucketId()).get();
        List<Item> itemOfBucket = newBucket.getItems();
        itemOfBucket.remove(item);
        bucketDao.update(newBucket);
    }

    @Override
    public void clear(Bucket bucket) throws DataProcessingException {
        Bucket tempBucket = bucketDao.get(bucket.getBucketId()).get();
        tempBucket.getItems().clear();
        bucketDao.update(tempBucket);
    }

    @Override
    public Bucket getByUserId(Long userId) throws DataProcessingException {
        Optional<Bucket> bucket = bucketDao.getByUserId(userId);
        if (bucket.isPresent()) {
            return bucket.get();
        }
        return create(new Bucket(userId));
    }
}
