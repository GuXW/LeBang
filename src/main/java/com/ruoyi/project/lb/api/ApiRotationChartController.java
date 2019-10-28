package com.ruoyi.project.lb.api;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.chart.domain.RotationChart;
import com.ruoyi.project.lb.chart.service.IRotationChartService;
import com.ruoyi.project.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Api("轮播图接口")
@Controller
@RequestMapping("/api/rotationChart")
public class ApiRotationChartController {

    @Autowired
    private IRotationChartService rotationChartService;
    @Autowired
    private RedisUtils redisUtils;


    @ApiOperation("获取所有未删除的轮播图")
    @GetMapping("/get_all_rotation")
    @ResponseBody
    public AjaxResult list()
    {
        Map<Object,Object> resultMap=redisUtils.hmget(RedisUtils.ROTATION_CHART_PATH);
        if (resultMap!=null&&!resultMap.isEmpty()){
            Collection<Object> valueCollection2 = resultMap.values();
            List<Object> valueList= new ArrayList<Object>(valueCollection2);//map转list
            return AjaxResult.success(valueList.toArray());
        }else {
            RotationChart rotationChart=new RotationChart();
            rotationChart.setStatus(1);
            List<RotationChart> list = rotationChartService.selectRotationChartList(rotationChart);
            for (RotationChart rotationChart1:
                    list) {
                redisUtils.hset(RedisUtils.ROTATION_CHART_PATH,rotationChart1.getId()+"",rotationChart1);
            }
            return AjaxResult.success(list.toArray());
            //list转Map
           // resultMap = list.stream().collect(Collectors.toMap(RotationChart::getId, RotationChart::getPicPath));
        }

    }



}
