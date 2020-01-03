package mate.academy.internetshop;

import mate.academy.internetshop.lib.Injector;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.User;

public class Main {
    static {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        User user = new User("Name");
        Bucket bucket = new Bucket();
        Item item1 = new Item("Item 1", 1.0);
        Item item2 = new Item("Item 2", 2.0);
        Item item3 = new Item("Item 3", 3.0);
    }
}
