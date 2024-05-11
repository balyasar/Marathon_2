package com.yasar.utility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Repository<T, ID> implements ICrud<T, ID> {
    private final EntityManagerFactory emf;
    private EntityManager em;
    private T t;

    public Repository(T entity) {
        emf = Persistence.createEntityManagerFactory("Search");
        em = emf.createEntityManager();
        this.t = entity;
    }

    private void openSession() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    private void closeSession() {
        em.getTransaction().commit();
        em.close();
    }

    private void rollBack() {
        em.getTransaction().rollback();
        em.close();
    }

    private void openSS() {
        if (!em.isOpen())
            em = emf.createEntityManager();
    }

    public void openEntityManager() {
        em = emf.createEntityManager();
    }

    @Override
    public T save(T entity) {
        openSession();
        em.persist(entity);
        closeSession();
        return entity;
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        try {
            openSession();
            entities.forEach(em::persist);
            closeSession();
        } catch (Exception e) {
            rollBack();
        }
        return entities;
    }

    /**
     * select * from tbl??? where id = ?
     */
    @Override
    public Optional<T> findByID(ID id) {
        openSS();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
        Root<T> root = (Root<T>) cq.from(t.getClass());
        cq.select(root); //select * from
        cq.where(cb.equal(root.get("id"), id));
        T result;
        try {
            //burada sorgumuz tek bir sonuç dönecek. Hiç dönmezse ilk bulduğu sonucu dönecek.
            result = em.createQuery(cq).getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existById(ID id) {
        openSS();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
        Root<T> root = (Root<T>) cq.from(t.getClass());
        cq.select(root);
        cq.where(cb.equal(root.get("id"), id));
        try {
            em.createQuery(cq).getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public List<T> findAll() {
        openSS();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
        Root<T> root = (Root<T>) cq.from(t.getClass());
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<T> findAllByFromColumnAndValues(String columnName, Object value) {
        openSS();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
        Root<T> root = (Root<T>) cq.from(t.getClass());
        cq.select(root);
        cq.where(cb.equal(root.get(columnName), value));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public void deleteById(ID id) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
//        Root<T> root = (Root<T>) cq.from(t.getClass());
//        cq.select(root);
//        cq.where(cb.equal(root.get("id"), id));
//        T removeElement;
//        try {
//            removeElement = em.createQuery(cq).getSingleResult();
//        } catch (NoResultException e) {
//            removeElement = null;
//        }
        findByID(id).ifPresent(removeElement -> {
            try {
                openSession();
                em.remove(removeElement);
                closeSession();
            } catch (Exception e) {
                if (em.isOpen()) {
                    rollBack();
                }
            }
        });
    }

    /**
     * Java Reflection
     * Long userId -> if(yserID != null) sorguya dahil et -> userId; value
     */
    @Override
    public List<T> findAllByEntity(T entity) {
        openSS();
        List<T> result;
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(t.getClass());
        Root<T> root = (Root<T>) cq.from(t.getClass());
        cq.select(root);
        /**
         * where
         * içeriği null olmayan değişkenlerin where içerisine predicate olarak eklenmesini sağlamak.
         */
        List<Predicate> predicateList = new ArrayList<>();
        for (int i = 1; i < fields.length; i++) { // entitu içinden aldığımız alanları dönüypruz.
            /**
             * DİKKAT !!!! Bir field erişim belirteçleri ile erişime kapalı olabilir. Bu nedenle
             * öncelikle bunları açmak gerekir.
             */
            fields[i].setAccessible(true);
            try {
                /**
                 * Erişime açtığımız fieldların adlarını ve değerlerini okuyoryz.
                 */
                String column = fields[i].getName();
                Object value = fields[i].get(entity);
                if (value != null) {
                    if (value instanceof String) {
                        predicateList.add(cb.like(root.get(column), "%" + value + "%"));
                    } else {
                        predicateList.add(cb.equal(root.get(column), value));
                    }
                }
            } catch (Exception e) {
                System.out.println("Hata oluştu ...... : " + e);
            }
        }
        cq.where(predicateList.toArray(new Predicate[]{}));
        result = em.createQuery(cq).getResultList();
        return result;
    }


}
