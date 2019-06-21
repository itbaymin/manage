package com.byc.dao.entity.movie;

import com.byc.dao.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by baiyc
 * 2019/5/31/031 20:30
 * Description：电影实体
 */
@Data
@Entity
@Table(name = "movie")
public class Movie extends BaseEntity {

    private String name;

    private String publish_time;

    private String cover;

    private String alias;

    private String language;

    private String douban;

    private String show_time;

    private String screen;

    private String director;

    private String stars;

    private String tag;

    private String description;

    private String icon;

    private String source;
}
