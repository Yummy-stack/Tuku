package com.tuku.controller.api.question;


import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.request.question.QuestionBankQuestionBatchAddRequest;
import com.tuku.tukuModel.request.question.QuestionBatchDeleteRequest;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.question.IQuestionBankQuestionService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.BaseResponse;
import com.tuku.tukucommon.annotation.AuthCheck;
import com.tuku.tukucommon.utils.ResultUtils;
import com.tuku.tukucommon.utils.ThrowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.tuku.tukucommon.constant.user.UserRoleConstant.ADMIN_ROLE;

@Api(value = "题库和题目之间的操作")
@RestController
@RequestMapping(value = "/question-bank-question")
public class QuestionBankQuestionController {
    @Resource
    private IUserService userService;

    @Resource
    private IQuestionBankQuestionService questionBankQuestionService;

    @ApiOperation(value = "题目批量添加到题库")
    @PostMapping(value = "/add/batch")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> batchAddQuestionsToBank(
            @RequestBody QuestionBankQuestionBatchAddRequest questionBankQuestionBatchAddRequest,
            HttpServletRequest request
    ) {
        // 参数校验
        ThrowUtils.throwIf(questionBankQuestionBatchAddRequest == null, ErrorCode.PARAMS_ERROR);
        LoginUserVo loginUser = userService.getLoginUser(request);
        Long questionBankId = questionBankQuestionBatchAddRequest.getQuestionBankId();
        List<Long> questionIdList = questionBankQuestionBatchAddRequest.getQuestionIdList();
        questionBankQuestionService.batchAddQuestionsToBank(questionIdList, questionBankId, loginUser);
        return ResultUtils.success(true);
    }

    @ApiOperation(value = "批量删除题目")
    @PostMapping(value = "/remove/batch")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> batchRemoveQuestionsFromBank(
            @RequestBody QuestionBatchDeleteRequest questionBatchDeleteRequest,
            HttpServletRequest request
    ) {
        if (questionBatchDeleteRequest == null) {
            throw new RuntimeException("参数为空");
        }
        LoginUserVo loginUser = userService.getLoginUser(request);
        questionBankQuestionService.batchRemoveQuestions(questionBatchDeleteRequest, loginUser);
        return ResultUtils.success(true);
    }
}
