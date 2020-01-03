package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;

@Dao
public class ItemDaoImpl implements ItemDao {
    @Override
    public Item create(Item item) {
        Storage.items.add(item);
        return item;
    }

    @Override
    public Optional<Item> get(Long itemId) {
        return Optional.ofNullable(Storage.items
                .stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Can't find item with id: "
                        + itemId)));
    }

    @Override
    public Item update(Item item) {
        int itemPos = 0;
        for (Item i : Storage.items) {
            if (i.getItemId().equals(item.getItemId())) {
                break;
            }
            itemPos++;
        }
        if (itemPos >= Storage.items.size()) {
            Storage.items.add(item);
        } else {
            Storage.items.set(itemPos, item);
        }
        return item;
    }

    @Override
    public boolean delete(Long itemId) {
        Optional optionalItem = Optional.ofNullable(Storage.items
                .stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst());
        if (optionalItem.isEmpty()) {
            return false;
        }
        Storage.items.remove(optionalItem.get());
        return true;
    }

    @Override
    public boolean deleteById(Item item) {
        return Storage.items.remove(item);
    }

    @Override
    public List<Item> getAllItems() {
        return Storage.items;
    }
}
