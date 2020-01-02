package mate.academy.internetshop.service;

import mate.academy.internetshop.model.User;

import java.util.Optional;

public interface UserService {
    User create(User user);

    Optional<User> get(Long userId);

    User update(User user);

    boolean delete(Long userId);

    boolean delete(User user);
}
