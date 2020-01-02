package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Bucket;

import java.util.List;
import java.util.Optional;

public interface BucketDao {
    Bucket create(Bucket bucket);

    Optional<Bucket> get(Long bucketId);

    List<Bucket> getAll();

    Bucket update(Bucket bucket);

    boolean delete(Long bucketId);

    boolean delete(Bucket bucket);
}
