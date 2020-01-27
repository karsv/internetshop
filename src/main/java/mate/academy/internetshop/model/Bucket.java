package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private Long userId;
    private Long bucketId;
    private List<Item> items;

    public Bucket(Long userId) {
        this.userId = userId;
        items = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Bucket{"
                + "bucketId="
                + bucketId
                + ", userId="
                + userId
                + ", items="
                + items
                + '}';
    }
}
