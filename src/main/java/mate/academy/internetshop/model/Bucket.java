package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

import mate.academy.internetshop.lib.GeneratorId;

public class Bucket {
    private Long bucketId;
    private Long orderId;
    private List<Item> items;

    public Bucket(Long orderId) {
        bucketId = GeneratorId.getNewBucketId();
        this.orderId = orderId;
        items = new ArrayList<>();
    }

    public Long getBucketId() {
        return bucketId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Bucket{"
                + "bucketId="
                + bucketId
                + ", orderId="
                + orderId
                + ", items="
                + items
                + '}';
    }
}
