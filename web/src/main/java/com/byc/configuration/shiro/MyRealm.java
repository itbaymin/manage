package com.byc.configuration.shiro;

import com.byc.configuration.jwt.JwtToken;
import com.byc.configuration.jwt.JwtUtil;
import com.byc.dao.system.UserRepository;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by baiyc
 * 2019/5/6/006 09:24
 * Description：realm
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    UserRepository userRepository;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String[] roles = JwtUtil.getArr(token, "roles");
        String[] perms = JwtUtil.getArr(token, "perms");
        info.setRoles(new HashSet<>(Arrays.asList(roles)));
        info.setStringPermissions(new HashSet<>(Arrays.asList(perms)));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
            throws AuthenticationException {
        JwtToken token = (JwtToken) auth;

        String principal = (String) token.getPrincipal();
        JwtUtil.verify(principal,JwtUtil.getStr(principal,"username"),JwtUtil.getArr(principal,"roles"),JwtUtil.getArr(principal,"perms"));

        return new SimpleAuthenticationInfo(principal, Boolean.TRUE, getName());
    }

}