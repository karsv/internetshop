package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Inject
    private static ItemDao itemDao;

    @Override
    public Item create(Item item) {
        return itemDao.create(item);
    }

    @Override
    public Optional<Item> get(Long itemId) {
        return itemDao.get(itemId);
    }

    @Override
    public Item update(Item item) {
        return itemDao.update(item);
    }

    @Override
    public List getAllItems() {
        return itemDao.getAllItems();
    }

    @Override
    public boolean delete(Long itemId) {
        return itemDao.delete(itemId);
    }

    @Override
    public boolean delete(Item item) {
        return itemDao.deleteById(item);
    }
}
