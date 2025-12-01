package com.tuku.service.user;

import com.baomidou.mybatisplus.extension.service.IService;

import com.tuku.domain.dto.user.UserLoginDto;
import com.tuku.domain.dto.user.UserRegisterDto;
import com.tuku.domain.pojo.user.User;
import com.tuku.domain.vo.LoginUserVo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author zhangyuxi
 * @since 2025-11-15
 */
public interface IUserService extends IService<User> {
    //用户注册
    boolean userRegister(UserRegisterDto userRegisterDto);
    //用户登录
    long userLogin(UserLoginDto userLoginDto, HttpServletRequest httpServletRequest);
    //加密算法（用户密码）
    String getEncryptPassword(String password);
    //判断解密后是否相等
    boolean verifyPassword(String rawPassword, String encodedPassword);
    //用户退出登录
    boolean userLogout(HttpServletRequest httpServletRequest);
    //获取当前登录用户信息
    LoginUserVo getLoginUser(HttpServletRequest httpServletRequest);
    //获取其他用户信息

}
