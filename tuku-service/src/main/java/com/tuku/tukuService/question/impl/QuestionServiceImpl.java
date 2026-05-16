package com.tuku.tukuService.question.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.QuestionMapper;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuService.question.IQuestionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目 服务实现类
 * </p>
 *
 * @author Yummy
 * @since 2026-05-16
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
