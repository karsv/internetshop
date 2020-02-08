package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.exceptions.DataProcessingException;

public interface GenericDao<T, N> {
    T create(T entity) throws DataProcessingException;

    Optional<T> get(N entityId) throws DataProcessingException;

    T update(T entity) throws DataProcessingException;

    void deleteById(N entityId) throws DataProcessingException;

    void delete(T entity) throws DataProcessingException;

    List<T> getAll() throws DataProcessingException;
}
