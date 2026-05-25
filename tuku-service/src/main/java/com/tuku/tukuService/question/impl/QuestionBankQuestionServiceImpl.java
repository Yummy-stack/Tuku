package com.tuku.tukuService.question.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionBankQuestionMapper;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.request.question.QuestionBatchDeleteRequest;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.question.IQuestionBankQuestionService;
import com.tuku.tukuService.question.IQuestionBankService;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements IQuestionBankQuestionService {
    @Resource
    private IQuestionService questionService;

    @Resource
    private IQuestionBankService questionBankService;

    @Resource
    private ThreadPoolTaskExecutor customExecutor;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, LoginUserVo loginUser) {
        ThrowUtils.throwIf(CollUtil.isEmpty(questionIdList), ErrorCode.PARAMS_ERROR, "题目列表为空");
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0, ErrorCode.PARAMS_ERROR, "题库非法");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        List<Question> questionList = questionService.listByIds(questionIdList);
        List<Long> validQuestionIdList = questionList.stream()
                .map(Question::getId)
                .collect(Collectors.toList());
        ThrowUtils.throwIf(CollUtil.isEmpty(validQuestionIdList), ErrorCode.PARAMS_ERROR, "合法的题目列表为空");

        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        List<QuestionBankQuestion> questionBankQuestions = validQuestionIdList.stream()
                .map(questionId -> fromQuestionAndBankToRelation(questionId, questionBankId, loginUser))
                .toList();

        List<List<QuestionBankQuestion>> splitQuestionBankQuestions = CollUtil.split(questionBankQuestions, 1000);
        ArrayList<CompletableFuture<Void>> futureArrayList = new ArrayList<>();
        for (List<QuestionBankQuestion> questionBankQuestionList : splitQuestionBankQuestions) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                this.saveBatch(questionBankQuestionList);
            }, customExecutor);
            futureArrayList.add(future);
        }
        CompletableFuture.allOf(futureArrayList.toArray(new CompletableFuture[0])).join();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveQuestions(QuestionBatchDeleteRequest questionBatchDeleteRequest, LoginUserVo loginUser) {
        if (questionBatchDeleteRequest == null) {
            throw new RuntimeException("参数错误");
        }
        if (loginUser == null) {
            throw new RuntimeException("用户未登录");
        }
        List<Long> questionIdList = questionBatchDeleteRequest.getQuestionIdList();
        if (questionIdList.isEmpty()) {
            throw new RuntimeException("请选择要删除的题目");
        }
        boolean removeQueResult = questionService.lambdaUpdate()
                .in(Question::getId, questionIdList)
                .remove();
    }

    public QuestionBankQuestion fromQuestionAndBankToRelation(Long questionId, Long questionBankId, LoginUserVo loginUser) {
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        questionBankQuestion.setQuestionBankId(questionBankId);
        questionBankQuestion.setQuestionId(questionId);
        questionBankQuestion.setUserId(loginUser.getId());

        return questionBankQuestion;
    }

}
