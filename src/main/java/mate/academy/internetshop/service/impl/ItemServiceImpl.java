package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Inject
    private static ItemDao itemDao;

    @Override
    public Item create(Item item) throws JdbcDaoException {
        return itemDao.create(item);
    }

    @Override
    public Optional<Item> get(Long itemId) throws JdbcDaoException {
        return itemDao.get(itemId);
    }

    @Override
    public Item update(Item item) throws JdbcDaoException {
        return itemDao.update(item);
    }

    @Override
    public List<Item> getAll() throws JdbcDaoException {
        return itemDao.getAll();
    }

    @Override
    public boolean deleteById(Long itemId) throws JdbcDaoException {
        return itemDao.deleteById(itemId);
    }

    @Override
    public boolean delete(Item item) throws JdbcDaoException {
        return itemDao.delete(item);
    }
}
