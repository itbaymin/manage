package com.byc.dao.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by baiyc
 * 2019/5/6/006 17:40
 * Description：users
 */
@Data
@Entity
@Table(name="users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

}
