package mate.academy.internetshop.service;

import java.util.Optional;

import mate.academy.internetshop.exceptions.AuthentificationException;
import mate.academy.internetshop.exceptions.JdbcDaoException;
import mate.academy.internetshop.model.User;

public interface UserService extends GenericService<User, Long> {
    User login(String login, String password) throws AuthentificationException, JdbcDaoException;

    String getToken();

    Optional<User> findByToken(String token) throws JdbcDaoException;
}
