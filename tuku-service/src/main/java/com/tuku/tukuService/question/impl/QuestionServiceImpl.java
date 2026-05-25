package com.tuku.tukuService.question.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionMapper;
import com.tuku.tukuModel.dto.question.*;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.question.QuePageVo;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.question.IQuestionBankQuestionService;
import com.tuku.tukuService.question.IQuestionBankService;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Resource
    private IUserService userService;

    @Resource
    private IQuestionBankService questionBankService;

    @Resource
    private IQuestionBankQuestionService questionBankQuestionService;

    @Override
    public Page<QuePageVo> listQuestionByPage(QueQueryDto queQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queQueryDto == null, ErrorCode.PARAMS_ERROR);

        String title = queQueryDto.getTitle();
        String content = queQueryDto.getContent();
        Long userId = queQueryDto.getUserId();
        String searchText = queQueryDto.getSearchText();
        int pageNum = queQueryDto.getPageNum();
        int pageSize = queQueryDto.getPageSize();

        Page<Question> questionPage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(title), Question::getTitle, title)
                .like(StrUtil.isNotBlank(content), Question::getContent, content)
                .eq(userId != null, Question::getUserId, userId)
                .and(StrUtil.isNotBlank(searchText), wrapper -> wrapper
                        .like(Question::getTitle, searchText)
                        .or()
                        .like(Question::getContent, searchText))
                .page(new Page<>(pageNum, pageSize));

        Page<QuePageVo> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(questionPage.getTotal());
        pageVO.setSize(questionPage.getSize());
        pageVO.setCurrent(questionPage.getCurrent());
        List<QuePageVo> quePageVoList = questionPage.getRecords().stream()
                .map(this::entityToPageVo)
                .collect(Collectors.toList());
        pageVO.setRecords(quePageVoList);

        return pageVO;
    }

    @Override
    public QuePageVo getQuestionDetail(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        Question question = this.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        QuePageVo quePageVo = entityToPageVo(question);
        return quePageVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addQuestion(QueAddDto queAddDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queAddDto == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question question = new Question();
        BeanUtil.copyProperties(queAddDto, question);
        question.setUserId(loginUser.getId());
        question.setCreateTime(new Date());
        question.setUpdateTime(new Date());
        question.setEditTime(new Date());

        this.save(question);
        return question.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestion(QueUpdDto queUpdDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdDto == null || queUpdDto.getId() == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(queUpdDto.getId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        Question question = new Question();
        BeanUtil.copyProperties(queUpdDto, question);
        question.setUpdateTime(new Date());
        question.setEditTime(new Date());

        return this.updateById(question);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        return this.removeById(id);
    }

    @Override
    public Page<QuePageVo> listQuestionByBankId(QueByBankQueryDto queByBankQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queByBankQueryDto == null, ErrorCode.PARAMS_ERROR);

        Long questionBankId = queByBankQueryDto.getQuestionBankId();
        ThrowUtils.throwIf(questionBankId == null || questionBankId <= 0, ErrorCode.PARAMS_ERROR, "题库id不能为空");

        QuestionBank questionBank = questionBankService.getById(questionBankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        String searchText = queByBankQueryDto.getSearchText();
        int pageNum = queByBankQueryDto.getPageNum();
        int pageSize = queByBankQueryDto.getPageSize();

        LambdaQueryWrapper<QuestionBankQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionBankQuestion::getQuestionBankId, questionBankId);

        Page<QuestionBankQuestion> questionBankQuestionPage = questionBankQuestionService.page(new Page<>(pageNum, pageSize), queryWrapper);

        List<Long> questionIdList = questionBankQuestionPage.getRecords().stream()
                .map(QuestionBankQuestion::getQuestionId)
                .collect(Collectors.toList());

        Page<QuePageVo> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(questionBankQuestionPage.getTotal());
        pageVO.setSize(questionBankQuestionPage.getSize());
        pageVO.setCurrent(questionBankQuestionPage.getCurrent());

        if (questionIdList.isEmpty()) {
            pageVO.setRecords(List.of());
            return pageVO;
        }

        List<Question> questionList = this.lambdaQuery()
                .in(Question::getId, questionIdList)
                .and(StrUtil.isNotBlank(searchText), wrapper -> wrapper
                        .like(Question::getTitle, searchText)
                        .or()
                        .like(Question::getContent, searchText))
                .list();

        List<QuePageVo> quePageVoList = questionList.stream()
                .map(this::entityToPageVo)
                .collect(Collectors.toList());
        pageVO.setRecords(quePageVoList);

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestionBank(QueUpdateBankDto queUpdateBankDto, HttpServletRequest request) {
        ThrowUtils.throwIf(queUpdateBankDto == null || queUpdateBankDto.getQuestionId() == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Long questionId = queUpdateBankDto.getQuestionId();
        Question question = this.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        List<Long> questionBankIds = queUpdateBankDto.getQuestionBankIds();

        questionBankQuestionService.remove(new LambdaQueryWrapper<QuestionBankQuestion>()
                .eq(QuestionBankQuestion::getQuestionId, questionId));

        if (questionBankIds != null && !questionBankIds.isEmpty()) {
            List<QuestionBankQuestion> questionBankQuestionList = questionBankIds.stream()
                    .map(questionBankId -> getQuesBankQueById(questionBankId, questionId, loginUser))
                    .collect(Collectors.toList());

            questionBankQuestionService.saveBatch(questionBankQuestionList);
        }

        return true;
    }

    private QuePageVo entityToPageVo(Question question) {
        if (question == null) {
            return null;
        }
        QuePageVo vo = new QuePageVo();
        BeanUtil.copyProperties(question, vo);
        return vo;
    }

    private QuestionBankQuestion getQuesBankQueById(Long questionBankId, Long questionId, LoginUserVo loginUser) {
        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
        questionBankQuestion.setQuestionBankId(questionBankId);
        questionBankQuestion.setQuestionId(questionId);
        questionBankQuestion.setUserId(loginUser.getId());
        questionBankQuestion.setCreateTime(new Date());
        questionBankQuestion.setUpdateTime(new Date());
        return questionBankQuestion;
    }

}


