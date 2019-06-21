package com.byc.configuration.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

/**
 * Created by baiyc
 * 2019/5/6/006 14:26
 * Description：JWT工具类
 **/
public class JwtUtil {

    private static final long EXPIRE_TIME = 5 * 60 * 1000;
    private static final String DEFAULT_SECRET = "mtdddin";

    public static boolean verify(String token,String username,String[] roles,String []perms){
        return verify(token,username,roles,perms,DEFAULT_SECRET);
    }

    public static boolean verify(String token, String username,String[] roles,String []perms, String secret) {
        try {
            if(StringUtils.isEmpty(username) || StringUtils.isEmpty(secret) || StringUtils.isEmpty(token) || StringUtils.isEmpty(roles) || StringUtils.isEmpty(perms))
                return false;
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withArrayClaim("roles", roles)
                    .withArrayClaim("perms", perms)
                    .build();
            //效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static String getStr(String token,String key) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(key).asString();
    }

    public static String[] getArr(String token,String key) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim(key).asArray(String.class);
    }

    public static String sign(String username, Set<String> roles, Set<String> permissions){
        return sign(username,roles,permissions,DEFAULT_SECRET);
    }

    public static String sign(String username, Set<String> roles, Set<String> permissions, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withArrayClaim("roles",roles.toArray(new String[roles.size()]))
                .withArrayClaim("perms",permissions.toArray(new String[permissions.size()]))
                .withExpiresAt(date)
                .sign(algorithm);

    }
}
