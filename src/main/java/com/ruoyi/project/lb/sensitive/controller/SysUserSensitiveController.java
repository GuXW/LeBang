package com.ruoyi.project.lb.sensitive.controller;

import java.util.List;

import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.project.content.FileTypeEnum;
import com.ruoyi.project.lb.sensitive.domain.EachTeam;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户敏感数据Controller
 * 
 * @author ${author}
 * @date 2019-10-08
 */
@Controller
@RequestMapping("/lb/sensitive")
public class SysUserSensitiveController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(SysUserSensitiveController.class);
    private String prefix = "lb/sensitive";

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Autowired
    private IUserService userService;

    @RequiresPermissions("lb:sensitive:view")
    @GetMapping()
    public String sensitive()
    {
        return prefix + "/sensitive";
    }

    @RequiresPermissions("lb:sensitive:view_admin")
    @GetMapping("/view_admin")
    public String sensitiveAdmin()
    {
        return prefix + "/sensitive_admin";
    }


    @RequiresPermissions("lb:sensitive:checkIdentity")
    @GetMapping("/check_identity")
    public String checkIdentity()
    {
        return prefix + "/sensitive_check";
    }


    /**
     * 查询用户敏感数据列表
     */
    @RequiresPermissions("lb:sensitive:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUserSensitive sysUserSensitive)
    {
        startPage();
        List<SysUserSensitive> list = sysUserSensitiveService.selectSysUserSensitiveList(sysUserSensitive);
        return getDataTable(list);
    }

    /**
     * 导出用户敏感数据列表
     */
    @RequiresPermissions("lb:sensitive:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUserSensitive sysUserSensitive)
    {
        List<SysUserSensitive> list = sysUserSensitiveService.selectSysUserSensitiveList(sysUserSensitive);
        ExcelUtil<SysUserSensitive> util = new ExcelUtil<SysUserSensitive>(SysUserSensitive.class);
        return util.exportExcel(list, "sensitive");
    }

    /**
     * 新增用户敏感数据
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户敏感数据
     */
    @RequiresPermissions("lb:sensitive:add")
    @Log(title = "用户敏感数据", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUserSensitive sysUserSensitive)
    {
        return toAjax(sysUserSensitiveService.insertSysUserSensitive(sysUserSensitive));
    }

    /**
     * 修改用户敏感数据
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(id);
        mmap.put("sysUserSensitive", sysUserSensitive);
        return prefix + "/edit";
    }

    /**
     * 修改用户收款数据
     */
    @GetMapping("/edit_collection_information/{id}")
    public String editCollectionInformation(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(id);
        mmap.put("sysUserSensitive", sysUserSensitive);
        return prefix + "/edit_collection_information.html";
    }


    /**
     * 修改用户敏感数据
     */
    @GetMapping("/edit_check/{id}")
    public String editCheck(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(id);
        mmap.put("sysUserSensitive", sysUserSensitive);
        return prefix + "/edit_check";
    }


    /**
     * 修改保存用户敏感数据
     */
    @RequiresPermissions("lb:sensitive:edit")
    @Log(title = "用户敏感数据", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysUserSensitive sysUserSensitive)
    {
        if (sysUserSensitive.getId()!=null && sysUserSensitive.getConfirmStatus()!=null){
            SysUserSensitive sysUserSensitiveTemp = sysUserSensitiveService.selectSysUserSensitiveById(sysUserSensitive.getId()) ;
            if(Integer.valueOf(sysUserSensitiveTemp.getConfirmStatus())==2){
                return error("已审核通过，不可修改状态");
            }else if(Integer.valueOf(sysUserSensitiveTemp.getConfirmStatus())==0&&Integer.valueOf(sysUserSensitive.getConfirmStatus())!=1){
                return error("未上传状态下只能修改为待审核状态");
            }else if(Integer.valueOf(sysUserSensitiveTemp.getConfirmStatus())==3&&Integer.valueOf(sysUserSensitive.getConfirmStatus()) == 2){
                return error("审核驳回状态下只能重新长传审核，不可审核通过");
            }
            //设置状态
            sysUserSensitive.setStatus(sysUserSensitive.getConfirmStatus());
            //如果状态是通过 更新用户名称
            if (Integer.valueOf(sysUserSensitive.getConfirmStatus()) == 2){
                User user=new User();
                user.setUserId(sysUserSensitiveTemp.getUserId());
                user.setUpdateBy(ShiroUtils.getLoginName());
                user.setUserName(sysUserSensitiveTemp.getIdCardName());
                int count = userService.updateUser(user);
                if (count<=0){
                    return AjaxResult.error("更新用户信息失败");
                }
            }
        }

        return toAjax(sysUserSensitiveService.updateSysUserSensitive(sysUserSensitive));
    }

    /**
     * 删除用户敏感数据
     */
    @RequiresPermissions("lb:sensitive:remove")
    @Log(title = "用户敏感数据", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysUserSensitiveService.deleteSysUserSensitiveByIds(ids));
    }

    /**
     * 我的下级用户（好友）
     */
    @GetMapping("/sensitive_sub/{id}")
    public String user(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("userId", id);
        return prefix + "/sensitive_sub";
    }

    /**
     * 我的团队
     */
    @GetMapping("/my_team/{id}")
    public String myTeam(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("userId", id);
        return prefix + "/sensitive_team";
    }

    @PostMapping("/my_team")
    @ResponseBody
    public TableDataInfo myTeam(SysUserSensitive sysUserSensitive)
    {
        List<EachTeam> list = null;
        try {
            list = sysUserSensitiveService.queryMyTeam(sysUserSensitive.getUserId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return getDataTable(list);
        }
        return getDataTable(list);
    }


    /**
     * 上传活动图片
     * @param receivingChartCopy
     * @return
     */
    @PostMapping("/upload_file")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile receivingChartCopy)
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getProfile()+ FileTypeEnum.RECEIVABLES.getPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, receivingChartCopy);
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
