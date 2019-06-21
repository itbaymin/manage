package com.byc.dao.entity.label;

import com.byc.dao.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by baiyc
 * 2019/6/3/003 20:40
 * Description：收藏夹类型
 */
@Data
@Entity
@Table(name = "collect_type")
public class CollectType extends BaseEntity {
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "collect_type_id")
    private List<Label> labels;
}
