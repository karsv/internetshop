package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private Long bucketId;
    private List<Item> items;

    public Bucket() {
        bucketId = GeneratorId.getNewBucketId();
        items = new ArrayList<>();
    }

    public Long getBucketId() {
        return bucketId;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Bucket{"
                + "bucketId="
                + bucketId
                + ", items="
                + items
                + '}';
    }
}
