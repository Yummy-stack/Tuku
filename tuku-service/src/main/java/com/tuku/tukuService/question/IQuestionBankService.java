package com.tuku.tukuService.question;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.tukuModel.dto.question.bank.QBankAddDto;
import com.tuku.tukuModel.dto.question.bank.QBankQueryDto;
import com.tuku.tukuModel.dto.question.bank.QBankUpdDto;
import com.tuku.tukuModel.entity.question.QuestionBank;
import com.tuku.tukuModel.vo.question.bank.QBankDetailVo;
import com.tuku.tukuModel.vo.question.bank.QBankPageVo;

import javax.servlet.http.HttpServletRequest;

public interface IQuestionBankService extends IService<QuestionBank> {

    /**
     * 分页获取题库列表
     * @param qBankQueryDto 查询条件
     * @param request HTTP请求
     * @return 分页结果
     */
    Page<QBankPageVo> listQuestionBankByPage(QBankQueryDto qBankQueryDto, HttpServletRequest request);

    /**
     * 获取题库详情
     * @param id 题库id
     * @param request HTTP请求
     * @return 题库详情
     */
    QBankDetailVo getQuestionBankDetail(Long id, HttpServletRequest request);

    /**
     * 添加题库
     * @param qBankAddDto 添加参数
     * @param request HTTP请求
     * @return 题库id
     */
    Long addQuestionBank(QBankAddDto qBankAddDto, HttpServletRequest request);

    /**
     * 更新题库
     * @param qBankUpdDto 更新参数
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean updateQuestionBank(QBankUpdDto qBankUpdDto, HttpServletRequest request);

    /**
     * 删除题库
     * @param id 题库id
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean deleteQuestionBank(Long id, HttpServletRequest request);
}
