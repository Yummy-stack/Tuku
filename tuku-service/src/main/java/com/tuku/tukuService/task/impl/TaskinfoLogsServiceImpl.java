package com.tuku.tukuService.task.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuku.tukuMapper.TaskinfoLogsMapper;
import com.tuku.tukuModel.entity.task.TaskinfoLogs;
import com.tuku.tukuService.task.ITaskinfoLogsService;
import org.springframework.stereotype.Service;


@Service
public class TaskinfoLogsServiceImpl extends ServiceImpl<TaskinfoLogsMapper, TaskinfoLogs> implements ITaskinfoLogsService {

}
