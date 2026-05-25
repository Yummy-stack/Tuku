package com.tuku.tukuService.question;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.tukuModel.entity.question.QuestionBankQuestion;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.request.question.QuestionBatchDeleteRequest;
import com.tuku.tukuModel.vo.user.LoginUserVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IQuestionBankQuestionService extends IService<QuestionBankQuestion> {

    @Transactional(rollbackFor = Exception.class)
    void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, LoginUserVo loginUser);

    void batchRemoveQuestions(QuestionBatchDeleteRequest questionBatchDeleteRequest, LoginUserVo loginUser);
}
