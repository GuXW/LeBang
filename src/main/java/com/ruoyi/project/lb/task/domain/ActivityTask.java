package com.ruoyi.project.lb.task.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 活动任务对象 lb_activity_task
 * 
 * @author u
 * @date 2019-10-14
 */
public class ActivityTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long id;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long cid;

    /** 更新人id */
    @Excel(name = "更新人id")
    private Long uid;

    /** 当前数据状态（1正常，0已删除） */
    @Excel(name = "当前数据状态", readConverterExp = "1=正常，0已删除")
    private Integer status;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String activityTitle;

    /** 活动图片路径 */
    @Excel(name = "活动图片路径")
    private String activityPic;

    /** 开奖日期 */
    @Excel(name = "开奖日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lotteryTime;

    /** 活动状态(1待开奖 2已开奖) */
    @Excel(name = "活动状态(1待开奖 2已开奖)")
    private Integer activityState;

    /** 活动介绍 */
    @Excel(name = "活动介绍")
    private String activityRemark;

    /** 中奖人数限定 */
    @Excel(name = "中奖人数限定")
    private Integer winnersNumber;

    //临时字段
    private String createName;
    private String updateName;
    private Integer participantNum;
    private Long userId;
    private ActivityTaskDetail activityTaskDetail;
    private String loveNum;
    private String[] winnerMobile;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCid(Long cid) 
    {
        this.cid = cid;
    }

    public Long getCid() 
    {
        return cid;
    }
    public void setUid(Long uid) 
    {
        this.uid = uid;
    }

    public Long getUid() 
    {
        return uid;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setActivityTitle(String activityTitle) 
    {
        this.activityTitle = activityTitle.trim();
    }

    public String getActivityTitle() 
    {
        return activityTitle;
    }
    public void setActivityPic(String activityPic) 
    {
        this.activityPic = activityPic.trim();
    }

    public String getActivityPic() 
    {
        return activityPic;
    }
    public void setLotteryTime(Date lotteryTime) 
    {
        this.lotteryTime = lotteryTime;
    }

    public Date getLotteryTime() 
    {
        return lotteryTime;
    }
    public void setActivityState(Integer activityState) 
    {
        this.activityState = activityState;
    }

    public Integer getActivityState() 
    {
        return activityState;
    }

    public String getActivityRemark() {
        return activityRemark;
    }

    public void setActivityRemark(String activityRemark) {
        this.activityRemark = activityRemark.trim();
    }

    public Integer getWinnersNumber() {
        return winnersNumber;
    }

    public void setWinnersNumber(Integer winnersNumber) {
        this.winnersNumber = winnersNumber;
    }


    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Integer getParticipantNum() {
        return participantNum;
    }

    public void setParticipantNum(Integer participantNum) {
        this.participantNum = participantNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActivityTaskDetail getActivityTaskDetail() {
        return activityTaskDetail;
    }

    public void setActivityTaskDetail(ActivityTaskDetail activityTaskDetail) {
        this.activityTaskDetail = activityTaskDetail;
    }

    public String getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(String loveNum) {
        this.loveNum = loveNum;
    }

    public String[] getWinnerMobile() {
        return winnerMobile;
    }

    public void setWinnerMobile(String[] winnerMobile) {
        this.winnerMobile = winnerMobile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createTime", getCreateTime())
            .append("cid", getCid())
            .append("updateTime", getUpdateTime())
            .append("uid", getUid())
            .append("status", getStatus())
            .append("activityTitle", getActivityTitle())
            .append("activityPic", getActivityPic())
            .append("lotteryTime", getLotteryTime())
            .append("activityState", getActivityState())
            .append("activityRemark", getActivityRemark())
            .append("winnersNumber", getWinnersNumber())
            .toString();
    }
}
