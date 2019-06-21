package com.byc.dao.system;

import com.byc.dao.entity.system.User;
import com.byc.dao.support.extend.CustomRepository;

/**
 * Created by baiyc
 * 2019/5/13/013 16:44
 * Description：
 */
public interface UserRepository extends CustomRepository<User,Long> {

    User findByName(String name);
}
