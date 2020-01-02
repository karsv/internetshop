package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item create(Item item);

    Optional<Item> get(Long itemId);

    Item update(Item item);

    boolean delete(Long itemId);

    boolean delete(Item item);

    List<Item> getAllItems();
}
