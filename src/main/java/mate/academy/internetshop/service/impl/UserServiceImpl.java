package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private static UserDao userDao;

    @Override
    public User create(User user) {
        user.setToken(getToken());
        return userDao.create(user);
    }

    @Override
    public Optional<User> get(Long userId) {
        return userDao.get(userId);
    }

    @Override
    public User update(User user) {
        userDao.update(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public boolean deleteById(Long userId) {
        return userDao.deleteById(userId);
    }

    @Override
    public boolean delete(User user) {
        return userDao.delete(user);
    }

    @Override
    public User login(String login, String password) throws AuthentificationException {
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
    public Optional<User> findByToken(String token) {
        return userDao.findByToken(token);
    }
}
