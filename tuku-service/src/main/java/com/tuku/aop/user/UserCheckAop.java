package com.tuku.aop.user;


import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.enums.user.UserRoleEnum;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.annotation.AuthCheck;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class UserCheckAop {

    @Resource
    private IUserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint, AuthCheck authCheck) {
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();

            LoginUserVo loginUser = userService.getLoginUser(httpServletRequest);
            String userRole = loginUser.getUserRole();
            String mustRole = authCheck.mustRole();

            UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustRoleEnum == null) {
                return proceedingJoinPoint.proceed();
            }
            //要么是用户 要么是管理员
            UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(userRole);
            ThrowUtils.throwIf(userRoleEnum == null, ErrorCode.PARAMS_ERROR);

            boolean noAdmin = (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum));
            ThrowUtils.throwIf(noAdmin, ErrorCode.PARAMS_ERROR, "无权限访问");

            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException("权限校验出现了异常");
        }
    }
}
