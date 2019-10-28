package com.ruoyi.project.lb.sensitive.domain;

import com.ruoyi.project.system.user.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 用户敏感数据对象 sys_user_sensitive
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public class SysUserSensitive extends User
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

    /** 身份证名称 */
    @Excel(name = "身份证名称")
    private String idCardName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCardNum;

    /** 身份证正面 */
    @Excel(name = "身份证正面")
    private String idCardPositive;

    /** 身份证反面 */
    @Excel(name = "身份证反面")
    private String idCardNegative;

    /** 收款方式账号 */
    private Integer receivingChannel;

    /** 收款账号 */
    @Excel(name = "收款账号")
    private String receivingAccount;

    /** 收款图片 */
    @Excel(name = "收款图片")
    private String receivingChart;

    /** 邀请码 */
    @Excel(name = "邀请码")
    private String invitationCode;

    /** 邀请二维码 */
    @Excel(name = "邀请二维码")
    private String invitationQrcode;

    /** 邀请人id */
    @Excel(name = "邀请人id")
    private Long invitationId;


    /** 用户当前任务等级 */
    @Excel(name = "用户当前任务等级")
    private Integer userLevel;

    /** 用户愛心值 */
    @Excel(name = "用户愛心值")
    private Double userLoveVal;

    /** 用户余额 */
    @Excel(name = "用户愛心值")
    private Double userAmount;

    @Excel(name = "预定金额")
    private Double predeterminedAmount;
    /** 任务凭证 */
    @Excel(name = "任务凭证")
    private String userCredentials;

    private Integer taskType;//(任务类型(1还款,2创业))

    private Date activationTime;

    /**临时字段**/
    private String createName;
    private String updateName;
    private String invitationName;
    private String confirmStatus;//(0未上传 1待审核 2审核成功 3审核失败 )
    private Integer payNoConfirmNum;//付款确认数

    private List<SysUserSensitive> userSensitiveList;//下级用户

    private SysUserSensitive inviter;//上级用户

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

    public void setIdCardName(String idCardName)
    {
        this.idCardName = idCardName.trim();
    }

    public String getIdCardName() 
    {
        return idCardName;
    }
    public void setIdCardNum(String idCardNum) 
    {
        this.idCardNum = idCardNum.trim();
    }

    public String getIdCardNum() 
    {
        return idCardNum;
    }
    public void setIdCardPositive(String idCardPositive) 
    {
        this.idCardPositive = idCardPositive.trim();
    }

    public String getIdCardPositive() 
    {
        return idCardPositive;
    }
    public void setIdCardNegative(String idCardNegative) 
    {
        this.idCardNegative = idCardNegative.trim();
    }

    public String getIdCardNegative() 
    {
        return idCardNegative;
    }
    public void setReceivingAccount(String receivingAccount) 
    {
        this.receivingAccount = receivingAccount.trim();
    }

    public String getReceivingAccount() 
    {
        return receivingAccount;
    }
    public void setReceivingChart(String receivingChart) 
    {
        this.receivingChart = receivingChart.trim();
    }

    public String getReceivingChart() 
    {
        return receivingChart;
    }
    public void setInvitationCode(String invitationCode) 
    {
        this.invitationCode = invitationCode;
    }

    public String getInvitationCode() 
    {
        return invitationCode;
    }
    public void setInvitationQrcode(String invitationQrcode) 
    {
        this.invitationQrcode = invitationQrcode;
    }

    public String getInvitationQrcode() 
    {
        return invitationQrcode;
    }
    public void setInvitationId(Long invitationId) 
    {
        this.invitationId = invitationId;
    }

    public Long getInvitationId() 
    {
        return invitationId;
    }

    public void setUserLevel(Integer userLevel)
    {
        this.userLevel = userLevel;
    }

    public Integer getUserLevel()
    {
        return userLevel;
    }

    public Double getUserLoveVal() {
        return userLoveVal;
    }

    public void setUserLoveVal(Double userLoveVal) {
        this.userLoveVal = userLoveVal;
    }

    public Double getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(Double userAmount) {
        this.userAmount = userAmount;
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

    public Double getPredeterminedAmount() {
        return predeterminedAmount;
    }

    public void setPredeterminedAmount(Double predeterminedAmount) {
        this.predeterminedAmount = predeterminedAmount;
    }

    public String getInvitationName() {
        return invitationName;
    }

    public void setInvitationName(String invitationName) {
        this.invitationName = invitationName;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public void setUserCredentials(String userCredentials)
    {
        this.userCredentials = userCredentials;
    }

    public String getUserCredentials()
    {
        return userCredentials;
    }

    public List<SysUserSensitive> getUserSensitiveList() {
        return userSensitiveList;
    }

    public void setUserSensitiveList(List<SysUserSensitive> userSensitiveList) {
        this.userSensitiveList = userSensitiveList;
    }

    public SysUserSensitive getInviter() {
        return inviter;
    }

    public void setInviter(SysUserSensitive inviter) {
        this.inviter = inviter;
    }

    public Integer getReceivingChannel() {
        return receivingChannel;
    }

    public void setReceivingChannel(Integer receivingChannel) {
        this.receivingChannel = receivingChannel;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getPayNoConfirmNum() {
        return payNoConfirmNum;
    }

    public void setPayNoConfirmNum(Integer payNoConfirmNum) {
        this.payNoConfirmNum = payNoConfirmNum;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
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
            .append("idCardName", getIdCardName())
            .append("idCardNum", getIdCardNum())
            .append("idCardPositive", getIdCardPositive())
            .append("idCardNegative", getIdCardNegative())
            .append("receivingAccount", getReceivingAccount())
            .append("receivingChart", getReceivingChart())
            .append("invitationCode", getInvitationCode())
            .append("invitationQrcode", getInvitationQrcode())
            .append("invitationId", getInvitationId())
            .append("userId", getUserId())
            .append("userLevel", getUserLevel())
            .append("userLoveVal", getUserLoveVal())
            .append("predeterminedAmount", getPredeterminedAmount())
            .append("userCredentials", getUserCredentials())
            .toString();
    }
}
