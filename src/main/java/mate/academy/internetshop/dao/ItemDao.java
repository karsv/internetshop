package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Item;

public interface ItemDao {
    Item create(Item item);

    Optional<Item> get(Long itemId);

    Item update(Item item);

    boolean deleteById(Long itemId);

    boolean delete(Item item);

    List<Item> getAllItems();
}
