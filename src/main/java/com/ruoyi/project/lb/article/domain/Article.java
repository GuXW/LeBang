package com.ruoyi.project.lb.article.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 文章管理对象 lb_article
 * 
 * @author ${author}
 * @date 2019-10-10
 */
public class Article extends BaseEntity
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

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String articleTitle;

    /** 文章内容 */
    @Excel(name = "文章内容")
    private String articleContent;

    //临时字段
    private String createName;
    private String updateName;

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
    public void setArticleTitle(String articleTitle) 
    {
        this.articleTitle = articleTitle.trim();
    }

    public String getArticleTitle() 
    {
        return articleTitle;
    }
    public void setArticleContent(String articleContent) 
    {
        this.articleContent = articleContent.trim();
    }

    public String getArticleContent() 
    {
        return articleContent;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName.trim();
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName.trim();
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
            .append("articleTitle", getArticleTitle())
            .append("articleContent", getArticleContent())
            .toString();
    }
}
