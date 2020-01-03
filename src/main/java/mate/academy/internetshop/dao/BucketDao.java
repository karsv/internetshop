package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Bucket;

public interface BucketDao {
    Bucket create(Bucket bucket);

    Optional<Bucket> get(Long bucketId);

    List<Bucket> getAll();

    Bucket update(Bucket bucket);

    boolean deleteById(Long bucketId);

    boolean delete(Bucket bucket);
}
