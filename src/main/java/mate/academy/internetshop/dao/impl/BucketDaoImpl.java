package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Bucket;

@Dao
public class BucketDaoImpl implements BucketDao {
    @Override
    public Bucket create(Bucket bucket) {
        Storage.buckets.add(bucket);
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long bucketId) {
        return Optional.ofNullable(Storage.buckets
                .stream()
                .filter(b -> b.getBucketId().equals(bucketId))
                .findFirst()
                .orElseThrow(()
                        -> new NoSuchElementException("Can't find bucket with id: "
                        + bucketId)));
    }

    @Override
    public List<Bucket> getAll() {
        return Storage.buckets;
    }

    @Override
    public Bucket update(Bucket bucket) {
        int bucketPos = 0;
        for (Bucket b : Storage.buckets) {
            if (b.getBucketId().equals(bucket.getBucketId())) {
                break;
            }
            bucketPos++;
        }
        if (bucketPos >= Storage.buckets.size()) {
            Storage.buckets.add(bucket);
        } else {
            Storage.buckets.set(bucketPos, bucket);
        }
        return bucket;
    }

    @Override
    public boolean deleteById(Long bucketId) {
        Optional optionalBucket = Optional.ofNullable(Storage.buckets
                .stream()
                .filter(i -> i.getBucketId().equals(bucketId))
                .findFirst());
        if (optionalBucket.isEmpty()) {
            return false;
        }
        Storage.buckets.remove(optionalBucket.get());
        return true;
    }

    @Override
    public boolean delete(Bucket bucket) {
        return Storage.buckets.remove(bucket);
    }
}
