package mate.academy.internetshop.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.lib.Inject;
import mate.academy.internetshop.lib.Service;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private static UserDao userDao;

    @Override
    public User create(User user) throws DataProcessingException {
        byte[] salt = HashUtil.getSalt();
        user.setPassword(HashUtil.hashPassword(user.getPassword(), salt));
        user.setSalt(salt);
        user.setToken(getToken());
        return userDao.create(user);
    }

    @Override
    public Optional<User> get(Long userId) throws DataProcessingException {
        return userDao.get(userId);
    }

    @Override
    public User update(User user) throws DataProcessingException {
        return userDao.update(user);
    }

    @Override
    public List<User> getAll() throws DataProcessingException {
        return userDao.getAll();
    }

    @Override
    public void deleteById(Long userId) throws DataProcessingException {
        userDao.deleteById(userId);
    }

    @Override
    public void delete(User user) throws DataProcessingException {
        userDao.delete(user);
    }

    @Override
    public User login(String login, String password) throws AuthentificationException,
            DataProcessingException {
        Optional<User> user = userDao.login(login);
        if (user.isEmpty() || !user.get().getPassword()
                .equals(HashUtil.hashPassword(password, user.get().getSalt()))) {
            throw new AuthentificationException("Wrong authentication parameters!");
        }
        return user.get();
    }

    private String getToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<User> findByToken(String token) throws DataProcessingException {
        return userDao.findByToken(token);
    }
}
