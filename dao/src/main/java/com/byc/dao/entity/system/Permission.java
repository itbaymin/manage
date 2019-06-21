package com.byc.dao.entity.system;

import com.byc.dao.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by baiyc
 * 2019/5/13/013 14:08
 * Description：
 */
@Data
@Entity
@Table(name = "sys_permission")
public class Permission extends BaseEntity {

    @Column(nullable = false, columnDefinition = "varchar(20) default '' COMMENT '权限名'")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(200) default '' COMMENT '备注'")
    private String remark;

    @Column(nullable = false, columnDefinition = "bigint(20) default 0  COMMENT '父级权限'")
    private Long parent_id;

    @Column(nullable = false, columnDefinition = "varchar(200) default '' COMMENT '权限字符'")
    private String permisssions;

    @Column(nullable = false, columnDefinition = "varchar(20) default 0 COMMENT '类型 0：目录   1：菜单   2：按钮'")
    private int type;

    @Column(nullable = false, columnDefinition = "varchar(100) default '' COMMENT '菜单图标'")
    private String icon;

    @Column(nullable = false, columnDefinition = "int(11) default 0 COMMENT '排序'")
    private int order_num;

    @Column(nullable = false, columnDefinition = "varchar(200) default '' COMMENT 'url'")
    private String url;

    @JsonBackReference
    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
    @JoinTable(name = "sys_role_permission",joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "permission_id",referencedColumnName = "id"))
    private List<Role> roles;
}
