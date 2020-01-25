package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private static UserDao userDao;

    @Inject
    private static BucketService bucketService;

    @Override
    public User create(User user) throws JdbcDaoException {
        user.setToken(getToken());
        return userDao.create(user);
    }

    @Override
    public Optional<User> get(Long userId) throws JdbcDaoException {
        return userDao.get(userId);
    }

    @Override
    public User update(User user) throws JdbcDaoException {
        userDao.update(user);
        return user;
    }

    @Override
    public List<User> getAll() throws JdbcDaoException {
        return userDao.getAll();
    }

    @Override
    public boolean deleteById(Long userId) throws JdbcDaoException {
        return userDao.deleteById(userId);
    }

    @Override
    public boolean delete(User user) throws JdbcDaoException {
        return userDao.delete(user);
    }

    @Override
    public User login(String login, String password) throws AuthentificationException,
            JdbcDaoException {
        Optional<User> user = userDao.login(login);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new AuthentificationException("Wrong authentification parameters!");
        }
        return user.get();
    }

    @Override
    public String getToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<User> findByToken(String token) throws JdbcDaoException {
        return userDao.findByToken(token);
    }
}
