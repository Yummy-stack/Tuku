package com.tuku.tukuService.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.tukuModel.dto.user.*;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.vo.user.LoginUserVo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface IUserService extends IService<User> {
    //用户注册
    boolean userRegister(UserRegisterDto userRegisterDto);

    //用户登录
    Long userLogin(UserLoginDto userLoginDto, HttpServletRequest httpServletRequest);

    //加密算法（用户密码）
    String getEncryptPassword(String password);

    //判断解密后是否相等
    boolean verifyPassword(String rawPassword, String encodedPassword);

    //用户退出登录
    boolean userLogout(HttpServletRequest httpServletRequest);

    //获取当前登录用户信息
    LoginUserVo getLoginUser(HttpServletRequest httpServletRequest);

    //获取其他用户信息
    LoginUserVo getAnotherLoginUser(Long userId);

    //管理员添加用户
    boolean userAdd(UserAddDto userAddDto);

    //管理员更新用户
    boolean userUpdate(UserUpdateDto userUpdateDto);

    //管理员查询用户
    List<User> userQuery(UserQueryDto userQueryDto);

    //管理员查询详细用户信息
    User userInformation(Long userId);

    //管理员查询所有用户信心
    Page<User> listUserVoByPage(UserQueryDto userQueryDto, HttpServletRequest request);

    //判断是否为管理员
    boolean isAdmin(User user);

    //用户进行签到
    boolean userSignsIn(Long userId);

    //用户查看签到表
    Map<LocalDate,Boolean> allUserSignIn(AllUserSignInDto allUserSignInDto);
}
