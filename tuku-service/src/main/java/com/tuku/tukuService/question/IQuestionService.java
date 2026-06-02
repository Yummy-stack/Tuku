package com.tuku.tukuService.question;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuku.es.query.QueEsDto;
import com.tuku.tukuModel.dto.question.*;
import com.tuku.tukuModel.entity.question.Question;
import com.tuku.tukuModel.entity.user.User;
import com.tuku.tukuModel.vo.question.QuePageVo;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IQuestionService extends IService<Question> {

    /**
     * 分页获取题目列表
     *
     * @param queQueryDto 查询条件
     * @param request     HTTP请求
     * @return 分页结果
     */
    Page<QuePageVo> listQuestionByPage(QueQueryDto queQueryDto, HttpServletRequest request);

    /**
     * 获取题目详情
     *
     * @param id      题目id
     * @param request HTTP请求
     * @return 题目详情
     */
    QuePageVo getQuestionDetail(Long id, HttpServletRequest request);

    /**
     * 添加题目
     *
     * @param queAddDto 添加参数
     * @param request   HTTP请求
     * @return 题目id
     */
    Long addQuestion(QueAddDto queAddDto, HttpServletRequest request);

    /**
     * 更新题目
     *
     * @param queUpdDto 更新参数
     * @param request   HTTP请求
     * @return 是否成功
     */
    boolean updateQuestion(QueUpdDto queUpdDto, HttpServletRequest request);

    /**
     * 删除题目
     *
     * @param id      题目id
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean deleteQuestion(Long id, HttpServletRequest request);

    /**
     * 根据题库id获取题目列表
     *
     * @param queByBankQueryDto 查询条件
     * @param request           HTTP请求
     * @return 分页结果
     */
    Page<QuePageVo> listQuestionByBankId(QueByBankQueryDto queByBankQueryDto, HttpServletRequest request);

    /**
     * 修改题目所属题库
     *
     * @param queUpdateBankDto 更新参数
     * @param request          HTTP请求
     * @return 是否成功
     */
    boolean updateQuestionBank(QueUpdateBankDto queUpdateBankDto, HttpServletRequest request);

    /**
     * 通过题目或者题目内容来检索 - 混合检索
     */
    List<Question> queryQueByHybridSearch(QueEsDto queEsDto);
}
