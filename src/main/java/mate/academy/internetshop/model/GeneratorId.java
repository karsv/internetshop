package mate.academy.internetshop.model;

public class GeneratorId {
    private static Long userId = Long.valueOf(0);
    private static Long bucketId = Long.valueOf(0);
    private static Long orderId = Long.valueOf(0);
    private static Long itemId = Long.valueOf(0);

    public static Long getNewUserId() {
        userId++;
        return userId;
    }

    public static Long getNewBucketId() {
        bucketId++;
        return bucketId;
    }

    public static Long getNewOrderId() {
        orderId++;
        return orderId;
    }

    public static Long getNewItemId() {
        itemId++;
        return itemId;
    }
}
