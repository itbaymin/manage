package com.byc.dao.support.extend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
/**
 * Created by baiyc
 * 2019/5/22/022 10:27
 * Description：抽象接口实现类
 */
public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements CustomRepository<T, ID> {

    private final EntityManager em;

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> listBySQL(String sql) {
        return em.createNativeQuery(sql).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> listByHQL(String hql) {
        return em.createQuery(hql).getResultList();
    }

    @Override
    public void updateBySql(String sql, Object...args) {
        Query query = em.createNativeQuery(sql);
        int i = 0;
        for(Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
    }

    @Override
    public void updateByHql(String hql, Object...args) {
        Query query = em.createQuery(hql);
        int i = 0;
        for(Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
    }

    @Override
    public Page<T> page(){
        PageRequest pageable = SystemRequestHolder.getPageRequest();
        return super.findAll(pageable);
    }
}