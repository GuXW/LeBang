package com.ruoyi.project.lb.chart.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 轮播图对象 lb_rotation_chart
 * 
 * @author ${author}
 * @date 2019-10-10
 */
public class RotationChart extends BaseEntity
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

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String picPath;

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
    public void setPicPath(String picPath) 
    {
        this.picPath = picPath.trim();
    }

    public String getPicPath() 
    {
        return picPath;
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
            .append("picPath", getPicPath())
            .toString();
    }
}
