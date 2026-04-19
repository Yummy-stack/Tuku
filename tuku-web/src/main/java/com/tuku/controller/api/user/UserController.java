package com.tuku.controller.api.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.tukuModel.dto.user.UserAddDto;
import com.tuku.tukuModel.dto.user.UserLoginDto;
import com.tuku.tukuModel.dto.user.UserQueryDto;
import com.tuku.tukuModel.dto.user.UserRegisterDto;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.annotation.AuthCheck;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.tuku.tukucommon.constant.user.UserRoleConstant.ADMIN_ROLE;


@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
@Api(tags = "用户模块的API")
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
        ThrowUtils.throwIf(paramNull, ErrorCode.NOT_FOUND_ERROR);

        Long userLoginResult = userService.userLogin(userLoginDto, httpServletRequest);
        return ResultUtils.success(userLoginResult);
    }


    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/login-user")
    BaseResponse<LoginUserVo> getLoginUser(HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(httpServletRequest == null, ErrorCode.PARAMS_ERROR, "传入request的参数为空");
        LoginUserVo loginUserVo = userService.getLoginUser(httpServletRequest);
        return ResultUtils.success(loginUserVo);
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest) {
        boolean isNull = httpServletRequest == null;
        ThrowUtils.throwIf(isNull, ErrorCode.PARAMS_ERROR, "传入request的参数为空");
        boolean userLogoutResult = userService.userLogout(httpServletRequest);
        return ResultUtils.success(userLogoutResult);
    }

    @ApiOperation(value = "管理员添加用户")
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> userAdd(UserAddDto userAddDto) {
        ThrowUtils.throwIf(userAddDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean saveResult = userService.userAdd(userAddDto);
        return ResultUtils.success(saveResult);
    }


    @ApiOperation(value = "管理员获取所有用户信息")
    @PostMapping("/list")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Page<User>> listUserVoByPage(@RequestBody UserQueryDto userQueryDto, HttpServletRequest request) {
        //校验参数
        ThrowUtils.throwIf(userQueryDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        //逻辑实现
        Page<User> loginUserVoPage = userService.listUserVoByPage(userQueryDto, request);
        //返回值
        return ResultUtils.success(loginUserVoPage);
    }

}
