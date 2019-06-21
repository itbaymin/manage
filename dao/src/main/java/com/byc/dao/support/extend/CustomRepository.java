package com.byc.dao.support.extend;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by baiyc
 * 2019/5/22/022 10:25
 * Description：抽象公共的Repository接口
 */
@NoRepositoryBean
@Transactional(readOnly=true)
public interface CustomRepository <T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    List<Object[]> listBySQL(String sql);
    List<Object[]> listByHQL(String hql);

    @Transactional
    void updateBySql(String sql, Object...args);
    @Transactional
    void updateByHql(String hql, Object...args);

    Page<T> page();
}
