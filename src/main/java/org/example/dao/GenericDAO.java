package org.example.dao;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public class GenericDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;
    private final EntityManagerFactory emf;

    public GenericDAO(
            Class<T> entityClass,
            EntityManagerFactory emf
    ) {
        this.entityClass = entityClass;
        this.emf = emf;
    }

    public T find(ID id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaQuery<T> query =
                    em.getCriteriaBuilder()
                            .createQuery(entityClass);
            query.select(query.from(entityClass));
            return em.createQuery(query).getResultList();
        } finally {
            em.close();
        }
    }

    public void create(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }

    public T update(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T resultat = em.merge(entity);
            tx.commit();
            return resultat;
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }

    public boolean delete(ID id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T entity = em.find(entityClass, id);

            if (entity == null) {
                tx.commit();
                return false;
            }

            em.remove(entity);
            tx.commit();
            return true;
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally {
            em.close();
        }
    }
}