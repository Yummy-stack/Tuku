package com.tuku.tukucommon.Interceptor;


import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukucommon.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.tuku.tukucommon.constant.user.UserLoginConstant.USER_LOGIN_STATE;


@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO bug修复 - 登录之后从Session拿到的user依旧是null
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        UserHolder.setUser(user.getId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
