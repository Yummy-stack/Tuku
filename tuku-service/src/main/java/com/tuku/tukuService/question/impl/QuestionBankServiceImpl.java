package com.tuku.tukuService.question.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionBankMapper;
import com.tuku.tukuModel.entity.question.bank.QuestionBank;
import com.tuku.tukuService.question.IQuestionBankService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题库 服务实现类
 * </p>
 *
 * @author Yummy
 * @since 2026-05-16
 */
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements IQuestionBankService {

}
