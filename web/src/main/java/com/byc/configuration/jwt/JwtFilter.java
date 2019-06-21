package com.byc.configuration.jwt;

import com.byc.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by baiyc
 * 2019/5/6/006 14:27
 * Description：
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    protected boolean isVerifyAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = null;
        if(req.getCookies()==null)
            return false;
        for (Cookie cookie:req.getCookies()){
            if("Authorization".equals(cookie.getName()))
                authorization = cookie.getValue();
        }
        return authorization != null;
    }

    protected void executeVerify(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String authorization = null;
        for (Cookie cookie:req.getCookies()){
            if("Authorization".equals(cookie.getName())) {
                authorization = cookie.getValue();
                Cookie newCookie = new Cookie(cookie.getName(),cookie.getValue());
                newCookie.setMaxAge(AuthController.COOKIE_MAX_AGE);
                cookie.setMaxAge(AuthController.COOKIE_MAX_AGE);
                resp.addCookie(cookie);
            }
        }
        getSubject(request,response).login(new JwtToken(authorization,req.getRemoteHost()));
        //设置用户信息到请求
        request.setAttribute("username",JwtUtil.getStr(authorization,"username"));
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (isVerifyAttempt(req, resp)) {
            try {
                executeVerify(request, response);
                return true;
            } catch (Exception e) {
                log.error("token verify error");
            }
        }
        String requestURI = req.getRequestURI();
        req.setAttribute("targetUrl",requestURI);
        forwardTo(req, resp,"/");
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void forwardTo(ServletRequest req, ServletResponse resp,String uri) {
        try {
            req.getRequestDispatcher(uri).forward(req,resp);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ServletException e) {
            log.error(e.getMessage());
        }
    }
}