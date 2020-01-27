package mate.academy.internetshop.dao;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;

public interface BucketDao extends GenericDao<Bucket, Long> {
    void clear(Bucket bucket) throws DataProcessingException;
}
