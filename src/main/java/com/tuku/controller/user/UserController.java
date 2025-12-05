package com.tuku.controller.user;


import com.tuku.annotation.AuthCheck;
import com.tuku.common.BaseResponse;
import com.tuku.model.dto.user.UserAddDto;
import com.tuku.model.dto.user.UserLoginDto;
import com.tuku.model.dto.user.UserRegisterDto;
import com.tuku.model.enums.error.ErrorCode;
import com.tuku.model.vo.LoginUserVo;
import com.tuku.service.user.IUserService;
import com.tuku.utils.ResultUtils;
import com.tuku.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.tuku.constant.user.UserRoleConstant.ADMIN_ROLE;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@Api(value = "用户模块的API")
public class UserController {

    private final IUserService userService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    BaseResponse<Boolean> userRegister(@RequestBody UserRegisterDto userRegisterDto) {
        boolean paramNull = userRegisterDto == null || userRegisterDto.getUserAccount() == null
                || userRegisterDto.getUserPassword() == null || userRegisterDto.getUserConfirmPassword() == null;
        ThrowUtils.throwIf(paramNull, ErrorCode.NOT_FOUND_ERROR);

        boolean userRegisterResult = userService.userRegister(userRegisterDto);
        return ResultUtils.success(userRegisterResult);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    BaseResponse<Long> userLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest httpServletRequest) {
        boolean paramNull = userLoginDto == null || userLoginDto.getUserAccount() == null
                || userLoginDto.getUserPassword() == null || httpServletRequest == null;
        ThrowUtils.throwIf(paramNull,ErrorCode.NOT_FOUND_ERROR);

        long userLoginResult = userService.userLogin(userLoginDto, httpServletRequest);
        return ResultUtils.success(userLoginResult);
    }


    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/login-user")
    BaseResponse<LoginUserVo> getLoginUser(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null,ErrorCode.PARAMS_ERROR,"传入request的参数为空");
        LoginUserVo loginUserVo = userService.getLoginUser(httpServletRequest);
        return ResultUtils.success(loginUserVo);
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest) {
        boolean isNull = httpServletRequest == null;
        ThrowUtils.throwIf(isNull,ErrorCode.PARAMS_ERROR,"传入request的参数为空");
        boolean userLogoutResult = userService.userLogout(httpServletRequest);
        return ResultUtils.success(userLogoutResult);
    }

    @ApiOperation(value = "管理员添加用户")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> userAdd(UserAddDto userAddDto) {
        ThrowUtils.throwIf(userAddDto == null,ErrorCode.PARAMS_ERROR,"参数为空");
        boolean saveResult = userService.userAdd(userAddDto);
        return  ResultUtils.success(saveResult);
    }



}
