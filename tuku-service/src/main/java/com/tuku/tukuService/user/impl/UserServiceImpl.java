package com.tuku.tukuService.user.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tuku.tukuMapper.UserMapper;
import com.tuku.tukuModel.dto.user.*;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.enums.user.UserRoleEnum;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.constant.user.UserRegisterConstant;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tuku.tukucommon.constant.user.UserLoginConstant.USER_LOGIN_STATE;



@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public boolean userRegister(UserRegisterDto userRegisterDto) {
        boolean paramNull = userRegisterDto == null || userRegisterDto.getUserAccount() == null
                || userRegisterDto.getUserPassword() == null || userRegisterDto.getUserConfirmPassword() == null;
        ThrowUtils.throwIf(paramNull, ErrorCode.NOT_FOUND_ERROR);

        boolean passwordEqual = !(userRegisterDto.getUserPassword().equals(userRegisterDto.getUserConfirmPassword()));
        ThrowUtils.throwIf(passwordEqual, ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");

        boolean exists = this.lambdaQuery()
                .eq(User::getUserAccount, userRegisterDto.getUserAccount())
                .exists();
        ThrowUtils.throwIf(exists, ErrorCode.PARAMS_ERROR, "用户已经注册过");

        User user = BeanUtil.copyProperties(userRegisterDto, User.class);
        String encryptPassword = this.getEncryptPassword(userRegisterDto.getUserPassword());
        ThrowUtils.throwIf(StrUtil.isBlank(encryptPassword), ErrorCode.PARAMS_ERROR);

        user.setUserPassword(encryptPassword);
        user.setUserAvatar(UserRegisterConstant.AVATAR);
        user.setUserName(UserRegisterConstant.NAME);
        user.setUserProfile(UserRegisterConstant.PROFILE);
        boolean saveResult = this.save(user);

        return saveResult;
    }

    @Override
    public Long userLogin(UserLoginDto userLoginDto, HttpServletRequest httpServletRequest) {
        boolean paramNull = userLoginDto == null || userLoginDto.getUserAccount() == null
                || userLoginDto.getUserPassword() == null || httpServletRequest == null;
        ThrowUtils.throwIf(paramNull, ErrorCode.NOT_FOUND_ERROR);

        User user = this.lambdaQuery()
                .eq(User::getUserAccount, userLoginDto.getUserAccount())
                .one();
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户不存在");

        boolean isLogin = this.verifyPassword(userLoginDto.getUserPassword(), user.getUserPassword());
        ThrowUtils.throwIf(!isLogin, ErrorCode.PARAMS_ERROR, "密码错误");

        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, user);

        return user.getId();
    }

    @Override
    public String getEncryptPassword(String password) {
        boolean flag = StrUtil.isBlank(password);
        ThrowUtils.throwIf(flag, ErrorCode.PARAMS_ERROR);
        String encryptPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return encryptPassword;
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        boolean allNotBlank = StrUtil.isAllNotBlank(rawPassword, encodedPassword);
        ThrowUtils.throwIf(!allNotBlank, ErrorCode.PARAMS_ERROR);
        boolean isCheckpw = BCrypt.checkpw(rawPassword, encodedPassword);
        return isCheckpw;
    }

    @Override
    public LoginUserVo getLoginUser(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null, ErrorCode.PARAMS_ERROR);
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        LoginUserVo loginUserVo = BeanUtil.copyProperties(user, LoginUserVo.class);
        return loginUserVo;
    }

    @Override
    public LoginUserVo getAnotherLoginUser(Long userId) {
        ThrowUtils.throwIf(userId == null, ErrorCode.NOT_FOUND_ERROR);
        User user = this.getById(userId);
        LoginUserVo loginUserVo = BeanUtil.copyProperties(user, LoginUserVo.class);
        return loginUserVo;
    }

    @Override
    public boolean userAdd(UserAddDto userAddDto) {
        ThrowUtils.throwIf(userAddDto == null, ErrorCode.NOT_FOUND_ERROR);
        User user = BeanUtil.copyProperties(userAddDto, User.class);
        boolean saveUser = this.save(user);
        return saveUser;
    }

    @Override
    public boolean userUpdate(UserUpdateDto userUpdateDto) {
        return false;
    }

    @Override
    public List<User> userQuery(UserQueryDto userQueryDto) {
        ThrowUtils.throwIf(userQueryDto == null, ErrorCode.NOT_FOUND_ERROR);
        Page<User> page = this.lambdaQuery()
                .page(new Page<>(userQueryDto.getPageNum(), userQueryDto.getPageSize()));
        long total = page.getTotal();
        List<User> records = page.getRecords();
        return records;
    }

    @Override
    public User userInformation(Long userId) {
        ThrowUtils.throwIf(userId == null, ErrorCode.NOT_FOUND_ERROR);
        User user = this.getById(userId);
        return user;
    }

    @Override
    public Page<User> listUserVoByPage(UserQueryDto userQueryDto, HttpServletRequest request) {
        //校验参数
        ThrowUtils.throwIf(userQueryDto == null, ErrorCode.PARAMS_ERROR, "传入参数为空");
        //逻辑实现
        LoginUserVo loginUser = getLoginUser(request);
        Page<User> userPage = this.lambdaQuery()
                .ne(User::getId, loginUser.getId())
                .eq(StrUtil.isNotBlank(userQueryDto.getUserAccount()), User::getUserAccount, userQueryDto.getUserAccount())
                .eq(StrUtil.isNotBlank(userQueryDto.getUserName()), User::getUserName, userQueryDto.getUserName())
                .page(new Page<>(userQueryDto.getPageNum(), userQueryDto.getPageSize()));
        //返回值
        return userPage;
    }

    @Override
    public boolean isAdmin(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR);
        return UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null, ErrorCode.PARAMS_ERROR, "传入request的参数为空");
        Object userObj = httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        ThrowUtils.throwIf(userObj == null, ErrorCode.PARAMS_ERROR, "用户未登录");
        httpServletRequest.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
}
