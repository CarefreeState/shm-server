package com.macaron.homeschool.interceptor;


import com.macaron.homeschool.common.annotation.Intercept;
import com.macaron.homeschool.common.annotation.handler.InterceptHelper;
import com.macaron.homeschool.common.context.BaseContext;
import com.macaron.homeschool.common.context.UserHelper;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author cattleYuan
 * @date 2024/1/18
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //可以解决拦截器跨域问题
        if (!(handler instanceof HandlerMethod)) {
            // 并不处理非目标方法的请求
            // todo: 例如通过本服务，但不是通过目标方法获取资源的请求，而这些请求需要进行其他的处理！
            return Boolean.TRUE;
        }
        // 获取目标方法
        Method targetMethod = ((HandlerMethod) handler).getMethod();
        // 获取 intercept 注解实例
        Intercept intercept = InterceptHelper.getIntercept(targetMethod);
        // 判断是否忽略
        if (InterceptHelper.isIgnore(intercept)) {
            return Boolean.TRUE;
        }
        UserHelper userHelper = null;
        try {
            userHelper = Optional.ofNullable(JwtUtil.parseJwt(request, response, UserHelper.class)).orElseGet(() -> {
                log.warn("用户未登录，token 为空");
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_LOGIN);
            });
        } catch (Exception e) {
            log.warn("登录失效：{}", e.getMessage());
            throw new GlobalServiceException("登录失效", GlobalServiceStatusCode.USER_NOT_LOGIN);
        }
        log.info("登录信息 -> {}", userHelper);
        //通过线程局部变量设置当前线程用户信息
        BaseContext.setCurrentUser(userHelper);
        // 记录接口的访问记录
        log.info("账户 {} 访问接口 {} ", userHelper.getUserId(), request.getRequestURI());
        // permit 中没有 role 就会抛异常
        if (!InterceptHelper.isValid(intercept, userHelper.getRole())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        return Boolean.TRUE;
    }

    // 此方法在全局响应异常处理器处理异常之后再执行，已经是大局已定了，对响应的写不会生效，抛出的异常也不会影响正常响应
    // 因为请求还没结束，这个方法的处理时间也在请求时间内，会影响响应速度
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        try {
        } finally {
            log.info("删除本地线程变量");
            BaseContext.removeCurrentUser();
        }
    }

}
