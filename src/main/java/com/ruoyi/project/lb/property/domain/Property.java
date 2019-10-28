package com.ruoyi.project.lb.property.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 平台配置对象 lb_property
 * 
 * @author u
 * @date 2019-10-10
 */
public class Property extends BaseEntity
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

    /** 配置类型 */
    @Excel(name = "配置类型")
    private Integer propertyType;

    /** 配置名称 */
    @Excel(name = "配置名称")
    private String propertyName;

    /** 配置值 */
    @Excel(name = "配置值")
    private String propertyValue;

    /** 配置值 */
    @Excel(name = "备注")
    private String remark;

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
    public void setPropertyType(Integer propertyType) 
    {
        this.propertyType = propertyType;
    }

    public Integer getPropertyType() 
    {
        return propertyType;
    }
    public void setPropertyName(String propertyName) 
    {
        this.propertyName = propertyName.trim();
    }

    public String getPropertyName() 
    {
        return propertyName;
    }
    public void setPropertyValue(String propertyValue) 
    {
        this.propertyValue = propertyValue.trim();
    }

    public String getPropertyValue() 
    {
        return propertyValue;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark.trim();
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
            .append("propertyType", getPropertyType())
            .append("propertyName", getPropertyName())
            .append("propertyValue", getPropertyValue())
            .append("remark", getRemark())
            .toString();
    }
}
