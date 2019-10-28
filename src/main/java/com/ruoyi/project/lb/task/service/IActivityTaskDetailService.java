package com.ruoyi.project.lb.task.service;

import com.ruoyi.project.lb.task.domain.ActivityTaskDetail;
import java.util.List;

/**
 * 活动明细Service接口
 * 
 * @author u
 * @date 2019-10-14
 */
public interface IActivityTaskDetailService 
{
    /**
     * 查询活动明细
     * 
     * @param id 活动明细ID
     * @return 活动明细
     */
    public ActivityTaskDetail selectActivityTaskDetailById(Long id);

    /**
     * 查询活动明细列表
     * 
     * @param activityTaskDetail 活动明细
     * @return 活动明细集合
     */
    public List<ActivityTaskDetail> selectActivityTaskDetailList(ActivityTaskDetail activityTaskDetail);

    /**
     * 新增活动明细
     * 
     * @param activityTaskDetail 活动明细
     * @return 结果
     */
    public int insertActivityTaskDetail(ActivityTaskDetail activityTaskDetail) throws Exception;

    /**
     * 修改活动明细
     * 
     * @param activityTaskDetail 活动明细
     * @return 结果
     */
    public int updateActivityTaskDetail(ActivityTaskDetail activityTaskDetail);

    /**
     * 批量删除活动明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteActivityTaskDetailByIds(String ids);

    /**
     * 删除活动明细信息
     * 
     * @param id 活动明细ID
     * @return 结果
     */
    public int deleteActivityTaskDetailById(Long id);

    /**
     * 更新未中奖用户状态
     * @param id
     */
    int updateNoWinnerByTaskId(Long id);
}
