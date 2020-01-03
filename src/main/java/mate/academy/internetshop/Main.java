package mate.academy.internetshop;

import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Injector;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;

public class Main {
    @Inject
    private static ItemService itemService;

    @Inject
    private static OrderService orderService;

    @Inject
    private static BucketService bucketService;

    @Inject
    private static UserService userService;

    static {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        User user = new User("Name");
        User user2 = new User("Name2");
        userService.create(user);
        userService.create(user2);
        System.out.println(Storage.users);
        userService.delete(user);
        System.out.println(Storage.users);

        Bucket bucket = new Bucket();
        bucketService.create(bucket);
        Item item1 = new Item("Item 1", 1.0);
        Item item2 = new Item("Item 2", 2.0);
        Item item3 = new Item("Item 3", 3.0);
        System.out.println(Storage.items);
        itemService.create(item1);
        itemService.create(item2);
        itemService.create(item3);
        bucketService.addItem(bucket, item1);
        bucketService.addItem(bucket, item2);
        bucketService.addItem(bucket, item3);
        System.out.println(Storage.buckets);
        orderService.completeOrder(bucketService.getAllItems(bucket), user);
        System.out.println(Storage.orders);
    }
}
