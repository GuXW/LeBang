package com.ruoyi.project.lb.task.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.project.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.task.mapper.ActivityTaskDetailMapper;
import com.ruoyi.project.lb.task.domain.ActivityTaskDetail;
import com.ruoyi.project.lb.task.service.IActivityTaskDetailService;
import com.ruoyi.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动明细Service业务层处理
 * 
 * @author u
 * @date 2019-10-14
 */
@Service
public class ActivityTaskDetailServiceImpl implements IActivityTaskDetailService 
{
    @Autowired
    private ActivityTaskDetailMapper activityTaskDetailMapper;

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询活动明细
     * 
     * @param id 活动明细ID
     * @return 活动明细
     */
    @Override
    public ActivityTaskDetail selectActivityTaskDetailById(Long id)
    {
        return activityTaskDetailMapper.selectActivityTaskDetailById(id);
    }

    /**
     * 查询活动明细列表
     * 
     * @param activityTaskDetail 活动明细
     * @return 活动明细
     */
    @Override
    public List<ActivityTaskDetail> selectActivityTaskDetailList(ActivityTaskDetail activityTaskDetail)
    {
        return activityTaskDetailMapper.selectActivityTaskDetailList(activityTaskDetail);
    }

    /**
     * 新增活动明细
     * 
     * @param activityTaskDetail 活动明细
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertActivityTaskDetail(ActivityTaskDetail activityTaskDetail) throws Exception
    {
        activityTaskDetail.setCreateTime(DateUtils.getNowDate());
        int count = activityTaskDetailMapper.insertActivityTaskDetail(activityTaskDetail);
        if (count>0){
            //扣除用户爱心值
            //获取爱心值
            String loveNum = redisUtils.hget(RedisUtils.PROPERTY, "love_num").toString();
            if (StringUtils.isBlank(loveNum)) {
                throw new Exception("获取不到爱心值配置");
            }
            //配置爱心值
            BigDecimal loveNumDecimal = new BigDecimal(Double.valueOf(loveNum));
            //获取当前用户
            Long userId = activityTaskDetail.getUserId();
            SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(userId);
            if (sysUserSensitive == null){
                throw new Exception("获取不到用户信息");
            }

            if (sysUserSensitive.getUserLoveVal()==null){
                throw new Exception("爱心值不足");
            }
            //用户爱心值
            BigDecimal userLoveVal = new BigDecimal(sysUserSensitive.getUserLoveVal());
            if (userLoveVal.compareTo(loveNumDecimal)<0){
                throw new Exception("爱心值不足");
            }

            SysUserSensitive sysUserSensitiveUpd=new SysUserSensitive();
            sysUserSensitiveUpd.setId(sysUserSensitive.getId());
            sysUserSensitiveUpd.setUserLoveVal(userLoveVal.subtract(loveNumDecimal).doubleValue());
            sysUserSensitiveService.updateSysUserSensitive(sysUserSensitiveUpd);
        }

        return count;
    }

    /**
     * 修改活动明细
     * 
     * @param activityTaskDetail 活动明细
     * @return 结果
     */
    @Override
    public int updateActivityTaskDetail(ActivityTaskDetail activityTaskDetail)
    {
        activityTaskDetail.setUpdateTime(DateUtils.getNowDate());
        return activityTaskDetailMapper.updateActivityTaskDetail(activityTaskDetail);
    }

    /**
     * 删除活动明细对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteActivityTaskDetailByIds(String ids)
    {
        return activityTaskDetailMapper.deleteActivityTaskDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除活动明细信息
     * 
     * @param id 活动明细ID
     * @return 结果
     */
    public int deleteActivityTaskDetailById(Long id)
    {
        return activityTaskDetailMapper.deleteActivityTaskDetailById(id);
    }

    @Override
    public int updateNoWinnerByTaskId(Long id) {
        return  activityTaskDetailMapper.updateNoWinnerByTaskId(id);
    }
}
