package com.ruoyi.project.lb.version.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * app版本号管理对象 lb_app_version
 * 
 * @author ${author}
 * @date 2019-10-23
 */
public class AppVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
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

    /** 版本号 */
    @Excel(name = "版本号")
    private String appVersion;

    /** app类型(1-android,2-ios,0-其他) */
    @Excel(name = "app类型(1-android,2-ios,0-其他)")
    private Integer appType;

    private String filePath;

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
    public void setAppVersion(String appVersion) 
    {
        this.appVersion = appVersion;
    }

    public String getAppVersion() 
    {
        return appVersion;
    }
    public void setAppType(Integer appType) 
    {
        this.appType = appType;
    }

    public Integer getAppType() 
    {
        return appType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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
            .append("appVersion", getAppVersion())
            .append("appType", getAppType())
            .append("remark", getRemark())
            .toString();
    }
}
