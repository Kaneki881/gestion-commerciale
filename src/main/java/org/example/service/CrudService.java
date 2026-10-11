package org.example.service;

import org.example.dao.GenericDAO;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CrudService<T, ID extends Serializable> {

    private final GenericDAO<T, ID> dao;

    public CrudService(
            Class<T> entityClass,
            EntityManagerFactory emf
    ) {
        this.dao = new GenericDAO<>(
                entityClass, emf
        );
    }

    public T getById(ID id) {
        return dao.find(Objects.requireNonNull(id));
    }

    public List<T> getAll() {
        return dao.findAll();
    }

    public void create(T entity) {
        dao.create(Objects.requireNonNull(entity));
    }

    public T update(T entity) {
        return dao.update(
                Objects.requireNonNull(entity)
        );
    }

    public boolean delete(ID id) {
        return dao.delete(Objects.requireNonNull(id));
    }
}