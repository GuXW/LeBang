package com.ruoyi.project.lb.version.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.project.utils.JsonUtils;
import com.ruoyi.project.utils.RedisUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.lb.version.domain.AppVersion;
import com.ruoyi.project.lb.version.service.IAppVersionService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

/**
 * app版本号管理Controller
 * 
 * @author ${author}
 * @date 2019-10-23
 */
@Controller
@RequestMapping("/lb/version")
public class AppVersionController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(AppVersionController.class);
    private String prefix = "lb/version";

    @Autowired
    private IAppVersionService appVersionService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ServerConfig serverConfig;


    @RequiresPermissions("lb:version:view")
    @GetMapping()
    public String version()
    {
        return prefix + "/version";
    }

    /**
     * 查询app版本号管理列表
     */
    @RequiresPermissions("lb:version:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AppVersion appVersion)
    {
        startPage();
        List<AppVersion> list = appVersionService.selectAppVersionList(appVersion);
        return getDataTable(list);
    }

    /**
     * 导出app版本号管理列表
     */
    @RequiresPermissions("lb:version:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AppVersion appVersion)
    {
        List<AppVersion> list = appVersionService.selectAppVersionList(appVersion);
        ExcelUtil<AppVersion> util = new ExcelUtil<AppVersion>(AppVersion.class);
        return util.exportExcel(list, "version");
    }

    /**
     * 新增app版本号管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存app版本号管理
     */
    @RequiresPermissions("lb:version:add")
    @Log(title = "app版本号管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AppVersion appVersion)
    {
        if (StringUtils.isBlank(appVersion.getAppVersion())){
            return error("版本号不能为空");
        }

        if (appVersion.getAppType()==null){
            return error("版本类型不能为空");
        }

        if (StringUtils.isBlank(appVersion.getFilePath())){
            return error("请上传安装包");
        }
        appVersion.setCid(ShiroUtils.getUserId());
        appVersion.setCreateTime(DateUtils.getNowDate());
        int count = appVersionService.insertAppVersion(appVersion);
        if (count>0){
            redisUtils.set("app_version_"+appVersion.getAppType(), JsonUtils.objectToJson(appVersion));
        }
        return toAjax(count);
    }

    /**
     * 修改app版本号管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        AppVersion appVersion = appVersionService.selectAppVersionById(id);
        mmap.put("appVersion", appVersion);
        return prefix + "/edit";
    }

    /**
     * 修改保存app版本号管理
     */
    @RequiresPermissions("lb:version:edit")
    @Log(title = "app版本号管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AppVersion appVersion)
    {
        return toAjax(appVersionService.updateAppVersion(appVersion));
    }

    /**
     * 删除app版本号管理
     */
    @RequiresPermissions("lb:version:remove")
    @Log(title = "app版本号管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(appVersionService.deleteAppVersionByIds(ids));
    }

    /**
     * 上传安装包文件
     * @param filePathCopy
     * @return
     */
    @PostMapping("/upload_file")
    @ResponseBody
    public AjaxResult uploadFile(@RequestParam( value="files",required=false)MultipartFile filePathCopy)
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getProfile()+"/install";
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, filePathCopy);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            log.error("文件上传错误:",e);
            return AjaxResult.error(e.getMessage());
        }
    }
}
