package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Inject
    private static ItemDao itemDao;

    @Override
    public Item create(Item item) throws DataProcessingException {
        return itemDao.create(item);
    }

    @Override
    public Optional<Item> get(Long itemId) throws DataProcessingException {
        return itemDao.get(itemId);
    }

    @Override
    public Item update(Item item) throws DataProcessingException {
        return itemDao.update(item);
    }

    @Override
    public List<Item> getAll() throws DataProcessingException {
        return itemDao.getAll();
    }

    @Override
    public void deleteById(Long itemId) throws DataProcessingException {
        itemDao.deleteById(itemId);
    }

    @Override
    public void delete(Item item) throws DataProcessingException {
        itemDao.delete(item);
    }
}
