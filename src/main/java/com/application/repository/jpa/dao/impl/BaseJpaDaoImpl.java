package com.application.repository.jpa.dao.impl;

import com.application.repository.jpa.dao.IBaseJpaDao;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Slf4j
public abstract class BaseJpaDaoImpl<T, ID> implements IBaseJpaDao<T, ID> {
    // 作用就是实体共用一个EntityManager
    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> clazz;

    public BaseJpaDaoImpl() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        //noinspection unchecked
        this.clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public T add(T t) {
        entityManager.persist(t);
        return t;
    }

    @Override
    public T update(T t) {
        return entityManager.merge(t);
    }

    @Override
    public void delete(ID id) {
        T t = entityManager.getReference(clazz, id);
        entityManager.remove(t);
    }

    @Override
    public int executeUpdate(String jPQL, Object... obj) {
        Query query = getQuery(jPQL, obj);
        return query.executeUpdate();
    }

    @Override
    public T load(ID id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public T load(String jPQL, Object... obj) {
        try {
            Query query = getQuery(jPQL, obj);
            //noinspection unchecked
            return (T) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private Query getQuery(String jPQL, Object[] obj) {
        Query query = entityManager.createQuery(jPQL);
        if (obj.length > 0) {
            for (int i = 0; i < obj.length; i++) {
                query.setParameter((i + 1), obj[i]);
            }
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getSimpleName()).getResultList();
    }

    @Override
    public List<T> find(String jPQL, Object... obj) {
        try {
            Query query = getQuery(jPQL, obj);
            //noinspection unchecked
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object findByAggregate(String jPQL, Object... obj) {
        return getQuery(jPQL, obj).getSingleResult();
    }

    @Override
    public int count() {
        Long num = (Long) entityManager.createQuery("select count(0) from " + clazz.getSimpleName()).getSingleResult();
        return num.intValue();
    }

    @Override
    public int count(String jPQL, Object... obj) {
        try {
            Query query = getQuery(jPQL, obj);
            Long num = (Long) query.getSingleResult();
            return num.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findPage(Integer page, Integer size) {
        return entityManager.createQuery("from " + clazz.getSimpleName()).setFirstResult(page).setMaxResults(size).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findPage(Integer page, Integer size, String jPQL, Object... obj) {
        try {
            return getQuery(jPQL, obj).setFirstResult(page).setMaxResults(size).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 结合实际的分页插架进行设置(尚未完成)
     *
     * @param page page实体
     * @param jPQL sql
     * @param obj  参数
     * @return Page<T>
     */
    @Override
    public Page<T> findPage(Page page, String jPQL, Object... obj) {
        Query query = getQuery(jPQL, obj);
        //int total = ((Long) query.getSingleResult()).intValue();
        query.setFirstResult(page.getNumber()).setMaxResults(page.getSize());
        //List<T> list = query.getResultList();
        //noinspection unchecked
        return page;
    }
}
