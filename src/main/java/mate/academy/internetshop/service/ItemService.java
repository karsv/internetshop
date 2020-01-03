package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.model.Item;

public interface ItemService {
    Item create(Item item);

    Optional<Item> get(Long itemId);

    Item update(Item item);

    List<Item> getAllItems();

    boolean deleteById(Long itemId);

    boolean delete(Item item);
}
