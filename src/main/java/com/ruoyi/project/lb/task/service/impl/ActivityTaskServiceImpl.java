package com.ruoyi.project.lb.task.service.impl;

import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.task.domain.ActivityTaskDetail;
import com.ruoyi.project.lb.task.service.IActivityTaskDetailService;
import com.ruoyi.project.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.task.mapper.ActivityTaskMapper;
import com.ruoyi.project.lb.task.domain.ActivityTask;
import com.ruoyi.project.lb.task.service.IActivityTaskService;
import com.ruoyi.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动任务Service业务层处理
 * 
 * @author u
 * @date 2019-10-14
 */
@Service
public class ActivityTaskServiceImpl implements IActivityTaskService 
{
    @Autowired
    private ActivityTaskMapper activityTaskMapper;
    @Autowired
    private IActivityTaskDetailService activityTaskDetailService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询活动任务
     * 
     * @param id 活动任务ID
     * @return 活动任务
     */
    @Override
    public ActivityTask selectActivityTaskById(Long id)
    {
        return activityTaskMapper.selectActivityTaskById(id);
    }

    /**
     * 查询活动任务列表
     * 
     * @param activityTask 活动任务
     * @return 活动任务
     */
    @Override
    public List<ActivityTask> selectActivityTaskList(ActivityTask activityTask)
    {
        return activityTaskMapper.selectActivityTaskList(activityTask);
    }

    /**
     * 新增活动任务
     * 
     * @param activityTask 活动任务
     * @return 结果
     */
    @Override
    public int insertActivityTask(ActivityTask activityTask)
    {
        activityTask.setCreateTime(DateUtils.getNowDate());
        return activityTaskMapper.insertActivityTask(activityTask);
    }

    /**
     * 修改活动任务
     * 
     * @param activityTask 活动任务
     * @return 结果
     */
    @Override
    public int updateActivityTask(ActivityTask activityTask)
    {
        activityTask.setUpdateTime(DateUtils.getNowDate());
        return activityTaskMapper.updateActivityTask(activityTask);
    }

    /**
     * 删除活动任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityTaskByIds(String ids)
    {
        return activityTaskMapper.deleteActivityTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除活动任务信息
     * 
     * @param id 活动任务ID
     * @return 结果
     */
    public int deleteActivityTaskById(Long id)
    {
        return activityTaskMapper.deleteActivityTaskById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult openWinner(Long id) throws Exception {
        //获取活动信息
        ActivityTask activityTask = this.selectActivityTaskById(id);
        if (activityTask == null) return AjaxResult.error("获取不到活动信息");
        if (activityTask.getActivityState().intValue()==2) return AjaxResult.error("该活动已开奖");
        //查看预设中奖人数
        Integer winnersNumber = activityTask.getWinnersNumber();
        //查看参与活动人数
        ActivityTaskDetail activityTaskDetail=new ActivityTaskDetail();
        activityTaskDetail.setTaskId(id);
        List<ActivityTaskDetail> activityTaskDetails = activityTaskDetailService.selectActivityTaskDetailList(activityTaskDetail);
        if (winnersNumber!=0&&activityTaskDetails.size()>0) {
            Set<Integer> set = new HashSet<Integer>();

            while (true) {
                // 调用Math.random()方法
                int num = (int) (Math.random() * (activityTaskDetails.size() - 0)) + 0;

                // 将不同的数存入HashSet中
                set.add(num);
                // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
                if (set.size() >= winnersNumber|| set.size()==activityTaskDetails.size()) {
                    break;
                }
            }
            //更新中奖人
            for (int a : set) {
                ActivityTaskDetail activityTaskDetailTemp = activityTaskDetails.get(a);
                //更新中奖人状态
                ActivityTaskDetail activityTaskDetailUpdate=new ActivityTaskDetail();
                activityTaskDetailUpdate.setId(activityTaskDetailTemp.getId());
                activityTaskDetailUpdate.setWinnerStatus(1);//更改中奖状态
                activityTaskDetailUpdate.setUid(ShiroUtils.getUserId());
                activityTaskDetailService.updateActivityTaskDetail(activityTaskDetailUpdate);
            }
            //更新余下用户为未中奖
            activityTaskDetailService.updateNoWinnerByTaskId(id);
        }

        //更新活动状态
        ActivityTask activityTaskUpdate=new ActivityTask();
        activityTaskUpdate.setId(activityTask.getId());
        activityTaskUpdate.setUid(ShiroUtils.getUserId());
        activityTaskUpdate.setActivityState(2);//已开奖
        activityTaskUpdate.setLotteryTime(DateUtils.getNowDate());
        int count = this.updateActivityTask(activityTaskUpdate);

        return AjaxResult.success(count);
    }

    @Override
    public List<ActivityTask> selectActivityTaskListByUserId(ActivityTask activityTask) {
        return activityTaskMapper.selectActivityTaskListByUserId(activityTask);
    }

    @Override
    public AjaxResult queryCurrentActivityTask(Long userId) {
        ActivityTask activityTask = activityTaskMapper.queryCurrentActivityTask();
        if (activityTask==null){
            return AjaxResult.success();
        }
        //获取当前用户是否有参与活动的数据
        ActivityTaskDetail activityTaskDetail=new ActivityTaskDetail();
        activityTaskDetail.setUserId(userId);
        activityTaskDetail.setTaskId(activityTask.getId());
        activityTaskDetail.setStatus(1);
        List<ActivityTaskDetail> activityTaskDetails = activityTaskDetailService.selectActivityTaskDetailList(activityTaskDetail);
        if (activityTaskDetails !=null && activityTaskDetails.size()>0){
            activityTask.setActivityTaskDetail(activityTaskDetails.get(0));
        }
        activityTask.setLoveNum(redisUtils.hget(RedisUtils.PROPERTY,"love_num").toString());


        if (activityTask.getActivityState()==2) {//如果是已开奖
            //查询中奖人名单
            ActivityTaskDetail activityTaskDetailTemp = new ActivityTaskDetail();
            activityTaskDetailTemp.setTaskId(activityTask.getId());
            activityTaskDetailTemp.setWinnerStatus(1);//中奖
            List<ActivityTaskDetail> activityTaskDetailsWinner = activityTaskDetailService.selectActivityTaskDetailList(activityTaskDetailTemp);
            if (activityTaskDetailsWinner !=null && activityTaskDetailsWinner.size()>0){
                String[] mobiles = new String[activityTaskDetailsWinner.size()];
                for (int i = 0; i < activityTaskDetailsWinner.size(); i++) {
                    StringBuffer sb = new StringBuffer(activityTaskDetailsWinner.get(i).getMobile());
                    sb.replace(3, 7, "****");
                    mobiles[i] = sb.toString();
                }
                activityTask.setWinnerMobile(mobiles);
            }
        }

        return AjaxResult.success(activityTask);
    }
}
