package com.ruoyi.project.lb.task.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.task.domain.ActivityTask;
import java.util.List;

/**
 * 活动任务Service接口
 * 
 * @author u
 * @date 2019-10-14
 */
public interface IActivityTaskService 
{
    /**
     * 查询活动任务
     * 
     * @param id 活动任务ID
     * @return 活动任务
     */
    public ActivityTask selectActivityTaskById(Long id);

    /**
     * 查询活动任务列表
     * 
     * @param activityTask 活动任务
     * @return 活动任务集合
     */
    public List<ActivityTask> selectActivityTaskList(ActivityTask activityTask);

    /**
     * 新增活动任务
     * 
     * @param activityTask 活动任务
     * @return 结果
     */
    public int insertActivityTask(ActivityTask activityTask);

    /**
     * 修改活动任务
     * 
     * @param activityTask 活动任务
     * @return 结果
     */
    public int updateActivityTask(ActivityTask activityTask);

    /**
     * 批量删除活动任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityTaskByIds(String ids);

    /**
     * 删除活动任务信息
     * 
     * @param id 活动任务ID
     * @return 结果
     */
    public int deleteActivityTaskById(Long id);

    /**
     * 开奖
     * @return
     */
    AjaxResult openWinner(Long id) throws Exception;

    /**
     * 根据用户id获取开奖记录
     * @param activityTask
     * @return
     */
    List<ActivityTask> selectActivityTaskListByUserId(ActivityTask activityTask);

    /**
     * 获取当前活动
     * @param userId
     * @return
     */
    AjaxResult queryCurrentActivityTask(Long userId);
}
