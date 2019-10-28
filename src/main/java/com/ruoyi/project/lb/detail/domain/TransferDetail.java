package com.ruoyi.project.lb.detail.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 用户转账明细对象 lb_transfer_detail
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public class TransferDetail extends BaseEntity
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

    /** 当前数据状态（1待付款，2已付款(待确认）,3已确认） */
    @Excel(name = "当前数据状态", readConverterExp = "1待付款，2已付款（待确认）,3已确认")
    private Integer status;

    /** 收款人id */
    @Excel(name = "收款人id")
    private Long toId;

    /** 付款人id */
    @Excel(name = "付款人id")
    private Long fromId;

    /** 上升等级 */
    @Excel(name = "上升等级")
    private Integer upLevel;

    /** 交易金额 */
    @Excel(name = "交易金额")
    private Double amount;

    /** 付款凭证上传路径 */
    private String paymentCertificate;

    /**临时字段*/
    private String fromName;
    private String fromIdCardNum;
    private String toName;
    private String toIdCardNum;
    private String receivingAccount;
    private String receivingChart;
    private Integer noStatus; //不包含当前状态



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
    public void setToId(Long toId) 
    {
        this.toId = toId;
    }

    public Long getToId() 
    {
        return toId;
    }
    public void setFromId(Long fromId) 
    {
        this.fromId = fromId;
    }

    public Long getFromId() 
    {
        return fromId;
    }
    public void setUpLevel(Integer upLevel) 
    {
        this.upLevel = upLevel;
    }

    public Integer getUpLevel() 
    {
        return upLevel;
    }
    public void setAmount(Double amount) 
    {
        this.amount = amount;
    }

    public Double getAmount() 
    {
        return amount;
    }

    public String getPaymentCertificate() {
        return paymentCertificate;
    }

    public void setPaymentCertificate(String paymentCertificate) {
        this.paymentCertificate = paymentCertificate.trim();
    }


    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName.trim();
    }

    public String getFromIdCardNum() {
        return fromIdCardNum;
    }

    public void setFromIdCardNum(String fromIdCardNum) {
        this.fromIdCardNum = fromIdCardNum.trim();
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName.trim();
    }

    public String getToIdCardNum() {
        return toIdCardNum;
    }

    public void setToIdCardNum(String toIdCardNum) {
        this.toIdCardNum = toIdCardNum.trim();
    }

    public String getReceivingAccount() {
        return receivingAccount;
    }

    public void setReceivingAccount(String receivingAccount) {
        this.receivingAccount = receivingAccount.trim();
    }

    public String getReceivingChart() {
        return receivingChart;
    }

    public void setReceivingChart(String receivingChart) {
        this.receivingChart = receivingChart.trim();
    }

    public Integer getNoStatus() {
        return noStatus;
    }

    public void setNoStatus(Integer noStatus) {
        this.noStatus = noStatus;
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
            .append("toId", getToId())
            .append("fromId", getFromId())
            .append("upLevel", getUpLevel())
            .append("amount", getAmount())
            .toString();
    }
}
