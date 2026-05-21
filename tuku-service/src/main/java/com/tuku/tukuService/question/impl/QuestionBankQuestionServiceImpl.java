package com.tuku.tukuService.question.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionBankQuestionMapper;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuService.question.IQuestionBankQuestionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题库题目 服务实现类
 * </p>
 *
 * @author Yummy
 * @since 2026-05-16
 */
@Service
public class QuestionBankQuestionServiceImpl extends ServiceImpl<QuestionBankQuestionMapper, QuestionBankQuestion> implements IQuestionBankQuestionService {

}
