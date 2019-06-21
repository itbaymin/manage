package com.byc.dao.entity.system;

import com.byc.dao.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by baiyc
 * 2019/5/13/013 14:06
 * Description：
 */
@Data
@Entity
@Table(name="sys_role")
public class Role extends BaseEntity{

    @Column(nullable = false, columnDefinition = "varchar(20) default '' COMMENT '角色名'")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(200) default '' COMMENT '备注'")
    private String remark;

    @JsonBackReference
    @ManyToMany(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinTable(name = "sys_user_role",joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),inverseJoinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")})
    private List<User> users;

    @ManyToMany(targetEntity = Permission.class,mappedBy = "roles",cascade = CascadeType.ALL)
    private List<Permission> permissions;
}
