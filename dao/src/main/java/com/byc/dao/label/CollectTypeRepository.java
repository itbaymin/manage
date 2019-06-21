package com.byc.dao.label;

import com.byc.dao.entity.label.CollectType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baiyc
 * 2019/6/3/003 19:51
 * Description：书签类型接口
 */
public interface CollectTypeRepository extends JpaRepository<CollectType,Integer> {
}
