package com.byc.dao.label;

import com.byc.dao.entity.label.Label;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baiyc
 * 2019/6/3/003 19:51
 * Description：书签接口
 */
public interface LabelRepository extends JpaRepository<Label,Integer> {

    Label findFirstByOrderByModifyTimeDesc();
}
