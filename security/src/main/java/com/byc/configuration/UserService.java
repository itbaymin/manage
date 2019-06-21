package com.byc.configuration;

import com.byc.dao.entity.system.Permission;
import com.byc.dao.entity.system.Role;
import com.byc.dao.entity.system.User;
import com.byc.dao.system.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baiyc
 * 2019/5/27/027 18:40
 * Description：
 */
@Component
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if(user!=null){
            List<GrantedAuthority> auth = new ArrayList();
            List<Role> roles = user.getRoles();
            roles.forEach(role->{
                List<Permission> perms = role.getPermissions();
                perms.forEach(perm ->
                        auth.add(new SimpleGrantedAuthority(perm.getName()))
                );
            });
            return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(), auth);
        }
        throw new UsernameNotFoundException("不存在该用户");
    }
}
