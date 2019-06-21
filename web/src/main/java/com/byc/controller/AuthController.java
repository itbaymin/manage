package com.byc.controller;

import com.byc.configuration.jwt.JwtUtil;
import com.byc.dao.entity.system.Permission;
import com.byc.dao.entity.system.Role;
import com.byc.dao.entity.system.User;
import com.byc.dao.system.PermissionRepository;
import com.byc.dao.system.UserRepository;
import com.byc.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by baiyc
 * 2019/5/5/005 17:27
 * Description：
 */
@Slf4j
@Controller
public class AuthController {

    public static final int COOKIE_MAX_AGE = 60*60;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    //@ResponseBody
    //@RequestMapping("/")
    //public Page<User> index(){
    //    UserQuery query = new UserQuery();
    //    query.setName("bai");
    //    query.setId(1);
    //    Pageable pageable = query.toPageable();
    //    return userRepository.findAll(query.toSpec(),pageable);
    //}

    @RequestMapping("/login")
    public String login(String username, String password, String targetUrl, Model model, HttpServletRequest request, HttpServletResponse response){
        try {
            User user = userRepository.findByName(username);
            if(user == null)
                throw new LoginException("账号不存在");
            if(!password.equals(user.getPassword()))
                throw new LoginException("密码输入错误");
            List<Role> roles = user.getRoles();
            Set<String> roleNames = roles.stream().map(role -> role.getName()).collect(Collectors.toSet());
            Set<String> permissions = new HashSet<>();
            roles.forEach(role -> {
                List<Permission> perm = role.getPermissions();
                perm.forEach(per -> permissions.add(per.getName()));
            });

            Cookie cookie = new Cookie("Authorization", JwtUtil.sign(username,roleNames,permissions));// 创建一个cookie，cookie的名字是key

            cookie.setDomain(request.getServerName());
            cookie.setPath(request.getContextPath());
            // 设置Cookie的有效期
            cookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cookie);
            if(StringUtils.isEmpty(targetUrl))
                targetUrl = "welcome";
            return "redirect:"+targetUrl;
        }catch (LoginException e){
            log.error(e.getMessage(),e);
            model.addAttribute("msg",e.getMessage());
        }
        return "index";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @ResponseBody
    @RequestMapping("/test")
    @RequiresPermissions("主")
    public String test(){
        return "Hello";
    }
}
