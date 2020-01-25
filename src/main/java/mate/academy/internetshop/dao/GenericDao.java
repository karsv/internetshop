package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.exceptions.JdbcDaoException;

public interface GenericDao<T, N> {
    T create(T entity) throws JdbcDaoException;

    Optional<T> get(N entityId) throws JdbcDaoException;

    T update(T entity) throws JdbcDaoException;

    boolean deleteById(N entityId) throws JdbcDaoException;

    boolean delete(T entity) throws JdbcDaoException;

    List<T> getAll() throws JdbcDaoException;
}
