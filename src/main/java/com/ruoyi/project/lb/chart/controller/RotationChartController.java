package com.ruoyi.project.lb.chart.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.utils.RedisUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.lb.chart.domain.RotationChart;
import com.ruoyi.project.lb.chart.service.IRotationChartService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * 轮播图Controller
 * 
 * @author ${author}
 * @date 2019-10-10
 */
@Controller
@RequestMapping("/lb/chart")
public class RotationChartController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(RotationChartController.class);

    private String prefix = "lb/chart";
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IRotationChartService rotationChartService;

    @RequiresPermissions("lb:chart:view")
    @GetMapping()
    public String chart()
    {
        return prefix + "/chart";
    }

    //启动程序初始化轮播图
    @PostConstruct
    public void initRotation(){
        RotationChart rotationChart=new RotationChart();
        rotationChart.setStatus(1);
        List<RotationChart> list = rotationChartService.selectRotationChartList(rotationChart);
        for (RotationChart rotationChart1:
             list) {
            redisUtils.hset(RedisUtils.ROTATION_CHART_PATH,rotationChart1.getId()+"",rotationChart1);
        }

    }

    /**
     * 查询轮播图列表
     */
    @RequiresPermissions("lb:chart:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RotationChart rotationChart)
    {
        startPage();
        List<RotationChart> list = rotationChartService.selectRotationChartList(rotationChart);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @RequiresPermissions("lb:chart:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RotationChart rotationChart)
    {
        List<RotationChart> list = rotationChartService.selectRotationChartList(rotationChart);
        ExcelUtil<RotationChart> util = new ExcelUtil<RotationChart>(RotationChart.class);
        return util.exportExcel(list, "chart");
    }

    /**
     * 新增轮播图
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存轮播图
     */
    @RequiresPermissions("lb:chart:add")
    @Log(title = "轮播图新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestParam("picPathfile") MultipartFile file)
    {
        RotationChart rotationChart=new RotationChart();
        try
        {
            if (!file.isEmpty())
            {
                String picPath = FileUploadUtils.upload(RuoYiConfig.getProfile()+"/rotation", file);
                rotationChart.setPicPath(picPath);
                rotationChart.setCid(ShiroUtils.getUserId());
                rotationChart.setCreateTime(DateUtils.getNowDate());
                rotationChart.setStatus(1);
                rotationChartService.insertRotationChart(rotationChart);
                redisUtils.hset(RedisUtils.ROTATION_CHART_PATH,rotationChart.getId()+"",rotationChart);
                return success();
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("新增轮播图失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 修改轮播图
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        RotationChart rotationChart = rotationChartService.selectRotationChartById(id);
        mmap.put("rotationChart", rotationChart);
        return prefix + "/edit";
    }

    /**
     * 修改保存轮播图
     */
    @RequiresPermissions("lb:chart:edit")
    @Log(title = "轮播图编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestParam("picPathfile") MultipartFile file,@RequestParam("id") String id)
    {

        RotationChart rotationChart=new RotationChart();
        try
        {

            if (!file.isEmpty())
            {
                String picPath = FileUploadUtils.upload(RuoYiConfig.getProfile()+"/rotation", file);
                rotationChart.setId(Long.valueOf(id));
                rotationChart.setPicPath(picPath);
                rotationChart.setUid(ShiroUtils.getUserId());
                rotationChart.setUpdateTime(DateUtils.getNowDate());
                rotationChartService.updateRotationChart(rotationChart);
                redisUtils.hset(RedisUtils.ROTATION_CHART_PATH,rotationChart.getId()+"",rotationChart);
                return success();
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("修改轮播图失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 删除轮播图
     */
    @RequiresPermissions("lb:chart:remove")
    @Log(title = "轮播图删除", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        int count = rotationChartService.deleteRotationChartByIds(ids);
        if (count>0){
            String[] split = ids.split(",");
            for (String id:
                 split) {
                redisUtils.hdel(RedisUtils.ROTATION_CHART_PATH,id);
            }
        }
        return toAjax(count);
    }
}
