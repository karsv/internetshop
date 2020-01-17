package mate.academy.internetshop.model;

public class GeneratorId {
    private static Long userId = 0L;
    private static Long bucketId = 0L;
    private static Long orderId = 0L;
    private static Long itemId = 0L;
    private static Long roleId = 0L;

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

    public static Long getNewRoleId() {
        roleId++;
        return roleId;
    }
}
