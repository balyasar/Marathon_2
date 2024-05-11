package com.yasar.utility;


import java.util.List;
import java.util.Optional;

public interface ICrud<T, ID> {
    T save(T entity);

    Iterable<T> saveAll(Iterable<T> entities); // List, Set

    Optional<T> findByID(ID id);

    boolean existById(ID id);

    List<T> findAll();

    /**
     * Bir post'un yorum listesini çekmek istiyorum.
     * select * from tblcomment where postid = ?
     */
    List<T> findAllByFromColumnAndValues(String columnName, Object value);

    void deleteById(ID id);

    List<T> findAllByEntity(T entity);

}
