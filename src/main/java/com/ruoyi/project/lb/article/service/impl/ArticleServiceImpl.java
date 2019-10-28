package com.ruoyi.project.lb.article.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.article.mapper.ArticleMapper;
import com.ruoyi.project.lb.article.domain.Article;
import com.ruoyi.project.lb.article.service.IArticleService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 文章管理Service业务层处理
 * 
 * @author ${author}
 * @date 2019-10-10
 */
@Service
public class ArticleServiceImpl implements IArticleService 
{
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 查询文章管理
     * 
     * @param id 文章管理ID
     * @return 文章管理
     */
    @Override
    public Article selectArticleById(Long id)
    {
        return articleMapper.selectArticleById(id);
    }

    /**
     * 查询文章管理列表
     * 
     * @param article 文章管理
     * @return 文章管理
     */
    @Override
    public List<Article> selectArticleList(Article article)
    {
        return articleMapper.selectArticleList(article);
    }

    /**
     * 新增文章管理
     * 
     * @param article 文章管理
     * @return 结果
     */
    @Override
    public int insertArticle(Article article)
    {
        article.setCreateTime(DateUtils.getNowDate());
        return articleMapper.insertArticle(article);
    }

    /**
     * 修改文章管理
     * 
     * @param article 文章管理
     * @return 结果
     */
    @Override
    public int updateArticle(Article article)
    {
        article.setUpdateTime(DateUtils.getNowDate());
        return articleMapper.updateArticle(article);
    }

    /**
     * 删除文章管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteArticleByIds(String ids)
    {
        return articleMapper.deleteArticleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文章管理信息
     * 
     * @param id 文章管理ID
     * @return 结果
     */
    public int deleteArticleById(Long id)
    {
        return articleMapper.deleteArticleById(id);
    }
}
