package com.zy.crm.interceptor;


import com.zy.crm.dao.UserMapper;
import com.zy.crm.exceptions.NoLoginException;
import com.zy.crm.service.UserService;
import com.zy.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* 非法访问拦截
* */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserMapper userMapper;
    /*
    *拦截用户是否是登录状态
    * 在目标方法（目标资源）执行前，执行的方法
    *
    * 返回布尔类型
    *       如果返回true，表示目标方法可以被执行
    *       如果返回false，表示阻止目标方法执行
    *
    * 判断用户是否是登录状态
    *   1.判断cookie中是否存在用户信息（获取用户ID）
    *   2.数据库中是否存在指定用户ID的值
    * 如果用户是登录状态，则允许目标方法执行；如果用户是非登陆状态，则抛出未登陆异常（在全局异常中做判断，如果是未登录异常，则跳转到登录页面）
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取cookie中的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //判断用户ID是否为空，且数据库中存在该用户ID的用户记录
        if(userId == null || userMapper.selectByPrimaryKey(userId) == null){
            //抛出未登录异常
            throw new NoLoginException();
        }
        return true;
    }
}
