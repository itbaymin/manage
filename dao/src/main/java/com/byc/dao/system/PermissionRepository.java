package com.byc.dao.system;

import com.byc.dao.entity.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baiyc
 * 2019/5/13/013 16:44
 * Description：
 */
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
