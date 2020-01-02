package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.User;

import java.util.NoSuchElementException;
import java.util.Optional;

@Dao
public class UserDaoImpl implements UserDao {
    @Override
    public User create(User user) {
        Storage.users.add(user);
        return user;
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
        Optional oldUser = Optional.ofNullable(Storage.users.stream()
                .filter(i -> i.getUserId().equals(user.getUserId()))
                .findFirst());
        Storage.users.remove(oldUser.get());
        Storage.users.add(user);
        return user;
    }

    @Override
    public boolean delete(Long userId) {
        Optional optionalUser = Optional.ofNullable(Storage.users
                .stream()
                .filter(i -> i.getUserId().equals(userId))
                .findFirst())
                .orElseThrow(() -> new NoSuchElementException("Can't find user with id: "
                        + userId));
        Storage.users.remove(optionalUser.get());
        return true;
    }

    @Override
    public boolean delete(User user) {
        if (!Storage.users.remove(user)) {
            throw new NoSuchElementException("Can't find user with id: "
                    + user.getUserId());
        }
        return true;
    }
}
