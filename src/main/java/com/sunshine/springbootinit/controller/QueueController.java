package com.sunshine.springbootinit.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.sunshine.springbootinit.annotation.AuthCheck;
import com.sunshine.springbootinit.common.BaseResponse;
import com.sunshine.springbootinit.common.DeleteRequest;
import com.sunshine.springbootinit.common.ErrorCode;
import com.sunshine.springbootinit.common.ResultUtils;
import com.sunshine.springbootinit.constant.CommonConstant;
import com.sunshine.springbootinit.constant.UserConstant;
import com.sunshine.springbootinit.exception.BusinessException;
import com.sunshine.springbootinit.exception.ThrowUtils;
import com.sunshine.springbootinit.manager.AiManager;
import com.sunshine.springbootinit.manager.RedisLimiterManager;
import com.sunshine.springbootinit.model.dto.chart.*;
import com.sunshine.springbootinit.model.entity.Chart;
import com.sunshine.springbootinit.model.entity.User;
import com.sunshine.springbootinit.model.vo.BiResponse;
import com.sunshine.springbootinit.service.ChartService;
import com.sunshine.springbootinit.service.UserService;
import com.sunshine.springbootinit.utils.ExcelUtils;
import com.sunshine.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.parsson.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 *
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({"dev","local"})
public class QueueController {

    // 自动注入一个线程池的实例
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;


    // 接收一个参数name，然后将任务添加到线程池中
    @GetMapping("/add")
    public void add(String name){
        // 使用CompletableFuture运行一个异步任务
        CompletableFuture.runAsync(() -> {
            // 打印一条日志信息，包括任务名称和执行线程的名称
            log.info("任务执行中: " + name + ",执行人:" + Thread.currentThread().getName());
            try {
                // 让线程休眠10分钟，模拟长时间运行的任务
                Thread.sleep(600000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            // 异步任务在threadPoolExecutor中执行
        },threadPoolExecutor);
    }

    // 该方法返回线程池的状态信息
    @GetMapping("/get")
    public String get(){
        // 创建一个HashMap存储线程池的状态信息
        HashMap<String, Object> map = new HashMap<>();
        // 获取线程池的队列长度
        int size = threadPoolExecutor.getQueue().size();
        // 将队列长度放入map中
        map.put("队列长度",size);
        // 获取线程池已接收的任务总数
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数",taskCount);
        // 获取线程池已完成的任务数
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数",completedTaskCount);
        // 获取线程池中正在执行任务的线程数
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数",activeCount);
        // 将map转换为JSON字符串并返回
        return JSONUtil.toJsonStr(map);

    }


}
