package com.tuku.controller.api.question;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.tukuModel.dto.question.*;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.question.QuePageVo;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.annotation.AuthCheck;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.tuku.tukucommon.constant.user.UserRoleConstant.ADMIN_ROLE;

@Api(value = "题目接口")
@RestController
@RequestMapping(value = "/question")
@Slf4j
public class QuestionController {

    @Resource
    private IQuestionService questionService;

    @ApiOperation(value = "分页获取题目列表")
    @PostMapping(value = "/page")
    BaseResponse<Page<QuePageVo>> listQuestionByPage(@RequestBody QueQueryDto queQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queQueryDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        Page<QuePageVo> pageResult = questionService.listQuestionByPage(queQueryDto, request);
        return ResultUtils.success(pageResult);
    }

    @ApiOperation(value = "获取题目详情")
    @PostMapping(value = "/detail")
    BaseResponse<QuePageVo> getQuestionDetail(@RequestBody QueDeleteDto queDeleteDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queDeleteDto == null || queDeleteDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        QuePageVo detailVo = questionService.getQuestionDetail(queDeleteDto.getId(), request);
        return ResultUtils.success(detailVo);
    }

    @ApiOperation(value = "管理员添加题目")
    @PostMapping(value = "/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Long> addQuestion(@RequestBody QueAddDto queAddDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queAddDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        Long id = questionService.addQuestion(queAddDto, request);
        return ResultUtils.success(id);
    }

    @ApiOperation(value = "管理员更新题目")
    @PostMapping(value = "/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> updateQuestion(@RequestBody QueUpdDto queUpdDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdDto == null || queUpdDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean result = questionService.updateQuestion(queUpdDto, request);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "管理员删除题目")
    @PostMapping(value = "/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> deleteQuestion(@RequestBody QueDeleteDto queDeleteDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queDeleteDto == null || queDeleteDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean result = questionService.deleteQuestion(queDeleteDto.getId(), request);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "管理员根据题库id获取题目列表")
    @PostMapping(value = "/list/by-bank")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Page<QuePageVo>> listQuestionByBankId(@RequestBody QueByBankQueryDto queByBankQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queByBankQueryDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        Page<QuePageVo> pageResult = questionService.listQuestionByBankId(queByBankQueryDto, request);
        return ResultUtils.success(pageResult);
    }

    @ApiOperation(value = "管理员修改题目所属题库")
    @PostMapping(value = "/update/bank")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> updateQuestionBank(@RequestBody QueUpdateBankDto queUpdateBankDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdateBankDto == null || queUpdateBankDto.getQuestionId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean result = questionService.updateQuestionBank(queUpdateBankDto, request);
        return ResultUtils.success(result);
    }
}
