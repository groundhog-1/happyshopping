package com.leyou.cart.interceptor;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */

@Slf4j
public  class UserInterceptor implements HandlerInterceptor {

    private JwtProperties prop;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public UserInterceptor(JwtProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取cookie
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());

        try{
            //解析token
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            //传递用户info
            //1.在拦截器、controller、service中共享的有thread、reques、域对象、spring
            //request.setAttribute("user",info);
            tl.set(info);
            //放行
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //最后用完删除，一定要清空
        tl.remove();
    }

    public static UserInfo getUser(){
        return tl.get();
    }
}
