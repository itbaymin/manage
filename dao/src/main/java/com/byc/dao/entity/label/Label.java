package com.byc.dao.entity.label;

import com.byc.dao.entity.BaseEntity;
import com.byc.dao.support.DateConverter;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by baiyc
 * 2019/6/3/003 19:45
 * Description：标签实体
 */
@Data
@Entity
@Table(name = "label")
public class Label extends BaseEntity {

    @Column(name = "collect_type_id")
    private Integer collectTypeId;

    private String title;

    private String icon;

    private String url;

    @Column
    @Convert(converter = DateConverter.class)
    private Date lastTime;

}
