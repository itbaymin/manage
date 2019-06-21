package com.byc.dao.entity.system;

import com.byc.dao.entity.BaseEntity;
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
@Table(name="sys_user")
public class User extends BaseEntity{
    @Column(nullable = false, columnDefinition = "varchar(20) default '' COMMENT '用户名'")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(20) default '' COMMENT '密码'")
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(20) default '' COMMENT '盐'")
    private String salt;

    private String email;

    private String tel;

    private String description;

    private String city;

    private String icon;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1 COMMENT '用户状态'")
    private Boolean status;

    @ManyToMany(targetEntity = Role.class,mappedBy = "users",cascade = {CascadeType.ALL})
    private List<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", status=" + status +
                '}';
    }
}
