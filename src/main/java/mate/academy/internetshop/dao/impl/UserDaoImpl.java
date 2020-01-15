package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.GeneratorId;
import mate.academy.internetshop.model.User;

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public User create(User user) {
        User tempUser = user;
        tempUser.setUserId(GeneratorId.getNewUserId());
        Storage.users.add(tempUser);
        return tempUser;
    }

    @Override
    public Optional<User> get(Long userId) {
        return Optional.ofNullable(Storage.users
                .stream()
                .filter(b -> b.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(()
                        -> new NoSuchElementException("Can't find user with id: "
                        + userId)));
    }

    @Override
    public User update(User user) {
        int userPos = 0;
        for (User u : Storage.users) {
            if (u.getUserId().equals(user.getUserId())) {
                break;
            }
            userPos++;
        }
        Storage.users.set(userPos, user);
        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        Optional optionalUser = Optional.ofNullable(Storage.users
                .stream()
                .filter(i -> i.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(()
                        -> new NoSuchElementException("Can't find user with id: "
                        + userId)));
        if (optionalUser.isPresent()) {
            return Storage.users.remove(optionalUser.get());
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        return Storage.users.remove(user);
    }

    @Override
    public List<User> getAll() {
        return Storage.users;
    }

    @Override
    public Optional<User> login(String login, String password)
            throws AuthentificationException {
        Optional<User> user = Storage.users
                .stream()
                .filter(u -> u.getName().equals(login))
                .findFirst();
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            throw new AuthentificationException("Wrong parameters!");
        }
        return user;
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Storage.users
                .stream()
                .filter(u -> u.getToken().equals(token))
                .findFirst();
    }
}
