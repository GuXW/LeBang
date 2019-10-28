package com.ruoyi.project.lb.task.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.project.lb.task.domain.ActivityTaskDetail;
import com.ruoyi.project.lb.task.service.IActivityTaskDetailService;
import com.ruoyi.project.utils.JsonUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.runners.model.FrameworkField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.lb.task.domain.ActivityTask;
import com.ruoyi.project.lb.task.service.IActivityTaskService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 活动任务Controller
 * 
 * @author u
 * @date 2019-10-14
 */
@Controller
@RequestMapping("/lb/task")
public class ActivityTaskController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ActivityTaskController.class);
    private String prefix = "lb/task";

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private IActivityTaskService activityTaskService;

    @Autowired
    private IActivityTaskDetailService activityTaskDetailService;

    @RequiresPermissions("lb:task:view")
    @GetMapping()
    public String task()
    {
        return prefix + "/task";
    }

    /**
     * 查询活动任务列表
     */
    @RequiresPermissions("lb:task:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityTask activityTask)
    {
        startPage();
        List<ActivityTask> list = activityTaskService.selectActivityTaskList(activityTask);
        return getDataTable(list);
    }

    /**
     * 开奖记录
     */
    @GetMapping("/lottery_record/{id}")
    public String user(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("userId", id);
        return prefix + "/task_by_user";
    }

    /**
     * 查询活动任务列表
     */
    @PostMapping("/list_by_userid")
    @ResponseBody
    public TableDataInfo listByUserId(ActivityTask activityTask)
    {
        List<ActivityTask> list = activityTaskService.selectActivityTaskListByUserId(activityTask);
        return getDataTable(list);
    }


    /**
     * 导出活动任务列表
     */
    @RequiresPermissions("lb:task:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityTask activityTask)
    {
        List<ActivityTask> list = activityTaskService.selectActivityTaskList(activityTask);
        ExcelUtil<ActivityTask> util = new ExcelUtil<ActivityTask>(ActivityTask.class);
        return util.exportExcel(list, "task");
    }

    /**
     * 新增活动任务
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存活动任务
     */
    @RequiresPermissions("lb:task:add")
    @Log(title = "活动任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityTask activityTask)
    {
        if (StringUtils.isEmpty(activityTask.getActivityTitle()) ){
            return AjaxResult.error("标题不能为空");
        }

        if (StringUtils.isEmpty(activityTask.getActivityRemark()) ){
            return AjaxResult.error("活动介绍不能为空");
        }

        if (StringUtils.isEmpty(activityTask.getActivityPic()) ){
            return AjaxResult.error("请先上传图片");
        }

        if (activityTask.getWinnersNumber()==null ){
            return AjaxResult.error("请设置中奖人数");
        }

        //查看是否有未开奖的活动
        ActivityTask activityTaskTemp=new ActivityTask();
        activityTaskTemp.setActivityState(1);
        List<ActivityTask> activityTasks = activityTaskService.selectActivityTaskList(activityTaskTemp);

        if (activityTasks!=null&&activityTasks.size()>0){
            return AjaxResult.error("有活动未开奖，请先开奖在增加新的活动");
        }

        activityTask.setCid(ShiroUtils.getUserId());
        activityTask.setCreateTime(DateUtils.getNowDate());

        return toAjax(activityTaskService.insertActivityTask(activityTask));
    }

    /**
     * 修改活动任务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ActivityTask activityTask = activityTaskService.selectActivityTaskById(id);
        //获取中奖人员
        ActivityTaskDetail activityTaskDetail=new ActivityTaskDetail();
        activityTaskDetail.setTaskId(id);
        activityTaskDetail.setWinnerStatus(1);//中奖
        List<ActivityTaskDetail> activityTaskDetails = activityTaskDetailService.selectActivityTaskDetailList(activityTaskDetail);
        List<String> mobiles= new ArrayList<>();
        if (activityTaskDetails!= null && activityTaskDetails.size()>0){
            for (ActivityTaskDetail activityTaskDetail1 :
                    activityTaskDetails ) {
                if (StringUtils.isNotBlank(activityTaskDetail1.getMobile())){
                    mobiles.add(new StringBuffer(activityTaskDetail1.getMobile()).replace(3, 7, "****").toString());
                }
            }
        }

        mmap.put("activityTask", activityTask);
        mmap.put("mobiles", JsonUtils.objectToJson(mobiles));
        return prefix + "/edit";
    }

    /**
     * 修改保存活动任务
     */
    @RequiresPermissions("lb:task:edit")
    @Log(title = "活动任务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityTask activityTask)
    {
        return toAjax(activityTaskService.updateActivityTask(activityTask));
    }

    /**
     * 删除活动任务
     */
    @RequiresPermissions("lb:task:remove")
    @Log(title = "活动任务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityTaskService.deleteActivityTaskByIds(ids));
    }

    /**
     * 上传活动图片
     * @param activityPicCopy
     * @return
     */
    @PostMapping("/upload_file")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile activityPicCopy)
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getProfile()+"/activityTaskPic";
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, activityPicCopy);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("文件上传错误:",e);
            return AjaxResult.error(e.getMessage());
        }
    }


    @PostMapping("/open_winner")
    @ResponseBody
    public AjaxResult uploadFile(Long id){
        System.out.println(id);
        if (id==null) return AjaxResult.error("请选择任务");
        try {
            return activityTaskService.openWinner(id);
        } catch (Exception e) {
            log.error("开奖错误:",e);
            return AjaxResult.error(e.getMessage());
        }
    }
}
