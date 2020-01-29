package mate.academy.internetshop.service.impl;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private static UserDao userDao;

    @Inject
    private static BucketService bucketService;

    @Override
    public User create(User user) throws DataProcessingException {
        User newUser = user;
        byte[] salt= HashUtil.getSalt();
        newUser.setPassword(HashUtil.hashPassword(user.getPassword(), salt));
        newUser.setSalt(salt);
        newUser.setToken(getToken());
        return userDao.create(newUser);
    }

    @Override
    public Optional<User> get(Long userId) throws DataProcessingException {
        return userDao.get(userId);
    }

    @Override
    public User update(User user) throws DataProcessingException {
        userDao.update(user);
        return user;
    }

    @Override
    public List<User> getAll() throws DataProcessingException {
        return userDao.getAll();
    }

    @Override
    public boolean deleteById(Long userId) throws DataProcessingException {
        return userDao.deleteById(userId);
    }

    @Override
    public boolean delete(User user) throws DataProcessingException {
        return userDao.delete(user);
    }

    @Override
    public User login(String login, String password) throws AuthentificationException,
            DataProcessingException {
        Optional<User> user = userDao.login(login);
        if (user.isEmpty() || !user.get().getPassword().
                equals(HashUtil.hashPassword(password, user.get().getSalt()))) {
            throw new AuthentificationException("Wrong authentification parameters!");
        }
        return user.get();
    }

    @Override
    public String getToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<User> findByToken(String token) throws DataProcessingException {
        return userDao.findByToken(token);
    }
}
