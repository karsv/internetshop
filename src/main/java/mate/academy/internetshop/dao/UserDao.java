package mate.academy.internetshop.dao;

import java.util.Optional;

import mate.academy.internetshop.model.User;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> login(String login);

    Optional<User> findByToken(String token);
}
