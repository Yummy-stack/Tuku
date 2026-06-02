package com.tuku.tukuService.question.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionBankMapper;
import com.tuku.tukuModel.dto.question.bank.QBankAddDto;
import com.tuku.tukuModel.dto.question.bank.QBankQueryDto;
import com.tuku.tukuModel.dto.question.bank.QBankUpdDto;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.enums.error.ErrorCode;
import com.tuku.tukuModel.vo.question.bank.QBankDetailVo;
import com.tuku.tukuModel.vo.question.bank.QBankPageVo;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import com.tuku.tukuService.question.IQuestionBankService;
import com.tuku.tukuService.question.IQuestionService;
import com.tuku.tukuService.user.IUserService;
import com.tuku.tukucommon.exception.BusinessException;
import com.tuku.tukucommon.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements IQuestionBankService {

    @Resource
    private IUserService userService;

    @Resource
    @Lazy
    private IQuestionService questionService;

    @Override
    public Page<QBankPageVo> listQuestionBankByPage(QBankQueryDto qBankQueryDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankQueryDto == null, ErrorCode.PARAMS_ERROR);

        String title = qBankQueryDto.getTitle();
        Long userId = qBankQueryDto.getUserId();
        int pageNum = qBankQueryDto.getPageNum();
        int pageSize = qBankQueryDto.getPageSize();

        Page<QuestionBank> questionBankPage = this.lambdaQuery()
                .like(StrUtil.isNotBlank(title), QuestionBank::getTitle, title)
                .eq(userId != null, QuestionBank::getUserId, userId)
                .page(new Page<>(pageNum, pageSize));

        Page<QBankPageVo> pageVO = new Page<>(pageNum, pageSize);
        pageVO.setTotal(questionBankPage.getTotal());
        pageVO.setSize(questionBankPage.getSize());
        pageVO.setCurrent(questionBankPage.getCurrent());
        List<QBankPageVo> qBankPageVoList = questionBankPage.getRecords().stream()
                .map(this::entityToPageVo)
                .collect(Collectors.toList());
        pageVO.setRecords(qBankPageVoList);

        return pageVO;
    }

    @Override
    public QBankDetailVo getQuestionBankDetail(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        QuestionBank questionBank = this.getById(id);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        QBankDetailVo detailVo = QBankDetailVo.entityToVo(questionBank);
        return detailVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addQuestionBank(QBankAddDto qBankAddDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankAddDto == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank questionBank = new QuestionBank();
        BeanUtil.copyProperties(qBankAddDto, questionBank);
        questionBank.setUserId(loginUser.getId());
        questionBank.setCreateTime(new Date());
        questionBank.setUpdateTime(new Date());
        questionBank.setEditTime(new Date());

        this.save(questionBank);
        return questionBank.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuestionBank(QBankUpdDto qBankUpdDto, HttpServletRequest request) {
        ThrowUtils.throwIf(qBankUpdDto == null || qBankUpdDto.getId() == null, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank oldQuestionBank = this.getById(qBankUpdDto.getId());
        ThrowUtils.throwIf(oldQuestionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        QuestionBank questionBank = new QuestionBank();
        BeanUtil.copyProperties(qBankUpdDto, questionBank);
        questionBank.setUpdateTime(new Date());
        questionBank.setEditTime(new Date());

        return this.updateById(questionBank);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestionBank(Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);

        LoginUserVo loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        QuestionBank oldQuestionBank = this.getById(id);
        ThrowUtils.throwIf(oldQuestionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");

        return this.removeById(id);
    }

    private QBankPageVo entityToPageVo(QuestionBank questionBank) {
        if (questionBank == null) {
            return null;
        }
        QBankPageVo vo = new QBankPageVo();
        BeanUtil.copyProperties(questionBank, vo);
        return vo;
    }
}
