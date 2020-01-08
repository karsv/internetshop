package mate.academy.internetshop.service;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, N> {
    T create(T entity);

    Optional<T> get(N entityId);

    T update(T entity);

    List<T> getAll();

    boolean deleteById(N entityId);

    boolean delete(T item);
}
