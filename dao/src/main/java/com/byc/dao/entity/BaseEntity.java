package com.byc.dao.entity;

import com.byc.dao.support.DateConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by baiyc
 * 2019/5/13/013 11:25
 * Description：
 */
@Data
@MappedSuperclass//使继承的子类有用数据库与属性的映射
@EntityListeners(AuditingEntityListener.class)//自动注入创建时间直接的监听
public abstract class BaseEntity implements Serializable{

    @Id
    @Column(length = 64, columnDefinition = "bigint(20) COMMENT '主键'")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(hidden = true)
    @Column(nullable = false, columnDefinition = "int(10) DEFAULT 0 COMMENT '创建日期'")
    @CreatedDate
    @Convert(converter = DateConverter.class)
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @Column(nullable = false, columnDefinition = "int(10) DEFAULT 0 COMMENT '修改日期'")
    @LastModifiedDate
    @Convert(converter = DateConverter.class)
    private Date modifyTime;

    public static final Sort DESC_BY_CREATE_DATE = Sort.by(new Sort.Order(Sort.Direction.DESC, "createDate"));
    public static final Sort ASC_BY_MODIFY_DATE = Sort.by(new Sort.Order(Sort.Direction.ASC, "modifyDate"));
}
