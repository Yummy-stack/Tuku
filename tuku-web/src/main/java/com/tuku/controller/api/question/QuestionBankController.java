package com.tuku.controller.api.question;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuku.tukuModel.dto.question.bank.*;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.question.bank.QBankDetailVo;
import com.tuku.tukuModel.vo.question.bank.QBankPageVo;
import com.tuku.tukuService.question.IQuestionBankService;
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

@Api(value = "题库接口")
@RestController
@RequestMapping(value = "/question-bank")
@Slf4j
public class QuestionBankController {

    @Resource
    private IQuestionBankService questionBankService;

    @ApiOperation(value = "分页获取题库列表")
    @PostMapping(value = "/page")
    BaseResponse<Page<QBankPageVo>> listQuestionBankByPage(@RequestBody QBankQueryDto qBankQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankQueryDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        Page<QBankPageVo> pageResult = questionBankService.listQuestionBankByPage(qBankQueryDto, request);
        return ResultUtils.success(pageResult);
    }

    @ApiOperation(value = "获取题库详情")
    @PostMapping(value = "/detail")
    BaseResponse<QBankDetailVo> getQuestionBankDetail(@RequestBody QBankDeleteDto qBankDeleteDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankDeleteDto == null || qBankDeleteDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        QBankDetailVo detailVo = questionBankService.getQuestionBankDetail(qBankDeleteDto.getId(), request);
        return ResultUtils.success(detailVo);
    }

    @ApiOperation(value = "管理员添加题库")
    @PostMapping(value = "/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Long> addQuestionBank(@RequestBody QBankAddDto qBankAddDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankAddDto == null, ErrorCode.PARAMS_ERROR, "参数为空");
        Long id = questionBankService.addQuestionBank(qBankAddDto, request);
        return ResultUtils.success(id);
    }

    @ApiOperation(value = "管理员更新题库")
    @PostMapping(value = "/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> updateQuestionBank(@RequestBody QBankUpdDto qBankUpdDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankUpdDto == null || qBankUpdDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean result = questionBankService.updateQuestionBank(qBankUpdDto, request);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "管理员删除题库")
    @PostMapping(value = "/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    BaseResponse<Boolean> deleteQuestionBank(@RequestBody QBankDeleteDto qBankDeleteDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankDeleteDto == null || qBankDeleteDto.getId() == null, ErrorCode.PARAMS_ERROR, "参数为空");
        boolean result = questionBankService.deleteQuestionBank(qBankDeleteDto.getId(), request);
        return ResultUtils.success(result);
    }
}
