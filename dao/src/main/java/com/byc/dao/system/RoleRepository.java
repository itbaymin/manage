package com.byc.dao.system;

import com.byc.dao.entity.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baiyc
 * 2019/5/13/013 16:44
 * Description：
 */
public interface RoleRepository extends JpaRepository<Role,Long> {
}
