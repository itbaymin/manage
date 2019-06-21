package com.byc.mapper;

import com.byc.dao.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baiyc
 * 2019/5/6/006 17:46
 * Description：
 */
public interface UserRepository extends JpaRepository<Users,Integer> {
}
