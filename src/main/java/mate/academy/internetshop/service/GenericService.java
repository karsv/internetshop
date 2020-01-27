package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.exceptions.DataProcessingException;

public interface GenericService<T, N> {
    T create(T entity) throws DataProcessingException;

    Optional<T> get(N entityId) throws DataProcessingException;

    T update(T entity) throws DataProcessingException;

    List<T> getAll() throws DataProcessingException;

    boolean deleteById(N entityId) throws DataProcessingException;

    boolean delete(T entity) throws DataProcessingException;
}
