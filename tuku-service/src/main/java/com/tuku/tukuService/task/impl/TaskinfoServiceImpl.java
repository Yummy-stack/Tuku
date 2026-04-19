package com.tuku.tukuService.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.TaskinfoMapper;
import com.tuku.tukuModel.entity.task.Taskinfo;
import com.tuku.tukuService.task.ITaskinfoService;
import org.springframework.stereotype.Service;


@Service
public class TaskinfoServiceImpl extends ServiceImpl<TaskinfoMapper, Taskinfo> implements ITaskinfoService {

}
