package com.byc.dao.support;

import com.byc.dao.entity.system.User;
import com.byc.dao.support.criteria.BaseQuery;
import com.byc.dao.support.criteria.MatchType;
import com.byc.dao.support.criteria.QueryWord;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by baiyc
 * 2019/5/22/022 14:51
 * Description：
 */
@Data
public class UserQuery extends BaseQuery<User> {

    @QueryWord(column = "id", func = MatchType.equal)
    private Integer id;

    @QueryWord(func = MatchType.like)
    private String name;

    @Override
    public Specification<User> toSpec() {
        return super.toSpecWithAnd();
    }
}
