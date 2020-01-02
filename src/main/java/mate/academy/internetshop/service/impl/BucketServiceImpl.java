package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;

import java.util.List;
import java.util.Optional;

@Service
public class BucketServiceImpl implements BucketService {

    @Inject
    private BucketDao bucketDao;

    @Inject
    private ItemDao itemDao;

    @Override
    public Bucket create(Bucket bucket) {
        bucketDao.create(bucket);
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        Optional<Bucket> bucket = bucketDao.get(bucketId);
        return bucket;
    }

    @Override
    public Bucket update(Bucket bucket) {
        return bucketDao.update(bucket);
    }

    @Override
    public boolean delete(Long bucketId) {
        return bucketDao.delete(bucketId);
    }

    @Override
    public boolean delete(Bucket bucket) {
        return bucketDao.delete(bucket);
    }

    @Override
    public void addItem(Bucket bucket, Item item) {
        Bucket newBucket = bucketDao.get(bucket.getBucketId()).get();
        Item item1 = itemDao.get(item.getItemId()).get();
        newBucket.getItems().add(item);
        bucketDao.update(newBucket);
    }

    @Override
    public boolean deleteItem(Bucket bucket, Item item) {
        Bucket newBucket = bucketDao.get(bucket.getBucketId()).get();
        List<Item> itemOfBucket = newBucket.getItems();
        itemOfBucket.remove(item);
        bucketDao.update(newBucket);
        return true;
    }

    @Override
    public void clear(Bucket bucket) {
        Bucket tempBucket = bucketDao.get(bucket.getBucketId()).get();
        tempBucket.getItems().clear();
        bucketDao.update(tempBucket);
    }

    @Override
    public Optional<List<Item>> getAllItems(Bucket bucket) {
        return Optional.ofNullable(bucketDao.get(bucket.getBucketId()).get().getItems());
    }
}
