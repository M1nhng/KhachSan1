package repository;

import java.util.List;

public interface IRepository<T> {

    List<T> getAll();

    T getById(String id);

    List<T> findByName(String ten);

    boolean add(T entity);

    boolean update(T entity);

    boolean delete(String id);
}