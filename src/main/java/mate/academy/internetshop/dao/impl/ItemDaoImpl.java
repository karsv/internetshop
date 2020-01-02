package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.Item;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Optional oldItem = Optional.ofNullable(Storage.items.stream()
                .filter(i -> i.getItemId().equals(item.getItemId()))
                .findFirst());
        Storage.items.remove(oldItem.get());
        Storage.items.add(item);
        return item;
    }

    @Override
    public boolean delete(Long itemId) {
        Optional optionalItemitem = Optional.ofNullable(Storage.items
                .stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst())
                .orElseThrow(() -> new NoSuchElementException("Can't find item with id: "
                        + itemId));
        Storage.items.remove(optionalItemitem.get());
        return true;
    }

    @Override
    public boolean delete(Item item) {
        if (!Storage.items.remove(item)) {
            throw new NoSuchElementException("Can't find such item: "
                    + item.getName());
        }
        return true;
    }

    @Override
    public List<Item> getAllItems() {
        return Storage.items;
    }
}
