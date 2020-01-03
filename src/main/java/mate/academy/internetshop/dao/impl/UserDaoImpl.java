package mate.academy.internetshop.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.db.Storage;
import mate.academy.internetshop.lib.Dao;
import mate.academy.internetshop.model.User;

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
        int userPos = 0;
        for (User u : Storage.users) {
            if (u.getUserId().equals(user.getUserId())) {
                break;
            }
            userPos++;
        }
        if (userPos >= Storage.users.size()) {
            Storage.users.add(user);
        } else {
            Storage.users.set(userPos, user);
        }
        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        Optional optionalUser = Optional.ofNullable(Storage.users
                .stream()
                .filter(i -> i.getUserId().equals(userId))
                .findFirst());
        if (optionalUser.isEmpty()) {
            return false;
        }
        Storage.users.remove(optionalUser.get());
        return true;
    }

    @Override
    public boolean delete(User user) {
        return Storage.users.remove(user);
    }
}
