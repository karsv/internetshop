package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, N> {
    T create(T entity);

    Optional<T> get(N itemId);

    T update(T entity);

    List<T> getAllEntities();

    boolean deleteById(N entityId);

    boolean delete(T entity);
}
