package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(Item item);

    Optional<Item> get(Long itemId);

    Item update(Item item);

    List getAllItems();

    boolean delete(Long itemId);

    boolean delete(Item item);
}
