package com.ruoyi.project.lb.article.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.lb.article.domain.Article;
import com.ruoyi.project.lb.article.service.IArticleService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 文章管理Controller
 * 
 * @author ${author}
 * @date 2019-10-10
 */
@Controller
@RequestMapping("/lb/article")
public class ArticleController extends BaseController
{
    private String prefix = "lb/article";

    @Autowired
    private IArticleService articleService;

    @RequiresPermissions("lb:article:view")
    @GetMapping()
    public String article()
    {
        return prefix + "/article";
    }

    /**
     * 查询文章管理列表
     */
    @RequiresPermissions("lb:article:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Article article)
    {
        startPage();
        List<Article> list = articleService.selectArticleList(article);
        return getDataTable(list);
    }

    /**
     * 导出文章管理列表
     */
    @RequiresPermissions("lb:article:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Article article)
    {
        List<Article> list = articleService.selectArticleList(article);
        ExcelUtil<Article> util = new ExcelUtil<Article>(Article.class);
        return util.exportExcel(list, "article");
    }

    /**
     * 新增文章管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存文章管理
     */
    @RequiresPermissions("lb:article:add")
    @Log(title = "文章管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Article article)
    {
        article.setCid(ShiroUtils.getUserId());
        article.setCreateTime(DateUtils.getNowDate());
        return toAjax(articleService.insertArticle(article));
    }

    /**
     * 修改文章管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Article article = articleService.selectArticleById(id);
        mmap.put("article", article);
        return prefix + "/edit";
    }

    /**
     * 修改保存文章管理
     */
    @RequiresPermissions("lb:article:edit")
    @Log(title = "文章管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Article article)
    {
        article.setUid(ShiroUtils.getUserId());
        article.setUpdateTime(DateUtils.getNowDate());
        return toAjax(articleService.updateArticle(article));
    }

    /**
     * 删除文章管理
     */
    @RequiresPermissions("lb:article:remove")
    @Log(title = "文章管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        if (StringUtils.isBlank(ids)){
            return AjaxResult.error("获取不到文章id");
        }
        String[] split = ids.split(",");
        //id 为1，2，3的文章不可删除
        boolean flag=false;
        for (String id:
             split) {
            if (id.equals("1")||id.equals("2")||id.equals("3")){
                flag=true;
            }
        }

        if (flag){
            return AjaxResult.error("不能删除联系我们，乐帮理念和社区问题文章");
        }

        return toAjax(articleService.deleteArticleByIds(ids));
    }
}
