package com.tuku.aop.user;

import com.tuku.annotation.AuthCheck;
import com.tuku.model.enums.error.ErrorCode;
import com.tuku.model.enums.user.UserRoleEnum;
import com.tuku.model.vo.user.LoginUserVo;
import com.tuku.service.user.IUserService;
import com.tuku.utils.ThrowUtils;
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
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint,AuthCheck authCheck) {
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
            ThrowUtils.throwIf(userRoleEnum == null,ErrorCode.PARAMS_ERROR);

            boolean noAdmin = (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum));
            ThrowUtils.throwIf(noAdmin,ErrorCode.PARAMS_ERROR,"无权限访问");

            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("权限校验出现了异常");
            throw new RuntimeException(e);
        }
    }
}
