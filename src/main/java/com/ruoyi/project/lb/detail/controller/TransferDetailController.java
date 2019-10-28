package com.ruoyi.project.lb.detail.controller;

import java.util.List;

import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import com.ruoyi.project.lb.detail.service.ITransferDetailService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 用户转账明细Controller
 * 
 * @author ${author}
 * @date 2019-10-08
 */
@Controller
@RequestMapping("/lb/detail")
public class TransferDetailController extends BaseController
{
    private String prefix = "lb/detail";

    @Autowired
    private ITransferDetailService transferDetailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @RequiresPermissions("lb:detail:view")
    @GetMapping()
    public String detail()
    {
        return prefix + "/detail";
    }

    /**
     * 查询用户转账明细列表
     */
    @RequiresPermissions("lb:detail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TransferDetail transferDetail)
    {
        startPage();
        List<TransferDetail> list = transferDetailService.selectTransferDetailList(transferDetail);
        return getDataTable(list);
    }

    @GetMapping("/list_by_payment/{userId}")
    public String listByPayment( @PathVariable("userId") Long userId,ModelMap mmap)
    {
        mmap.put("fromId",userId);
        return prefix + "/detail_payment";
    }

    @GetMapping("/list_by_collection/{userId}")
    public String listByCollection( @PathVariable("userId") Long userId,ModelMap mmap)
    {
        mmap.put("toId",userId);
        return prefix + "/detail_collection";
    }


    /**
     * 查询当前用户打款列表
     */
    @PostMapping("/list_by_payment")
    @ResponseBody
    public TableDataInfo listByPayment(TransferDetail transferDetail)
    {
        List<TransferDetail> list = transferDetailService.selectTransferDetailList(transferDetail);
        return getDataTable(list);
      /*  mmap.put("paymentList",list);
        return prefix + "/edit";*/
    }

    /**
     * 导出用户转账明细列表
     */
    @RequiresPermissions("lb:detail:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TransferDetail transferDetail)
    {
        List<TransferDetail> list = transferDetailService.selectTransferDetailList(transferDetail);
        ExcelUtil<TransferDetail> util = new ExcelUtil<TransferDetail>(TransferDetail.class);
        return util.exportExcel(list, "detail");
    }

    /**
     * 新增用户转账明细
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("sysUser", userService.selectUserList(new User()));
        return prefix + "/add";
    }

    /**
     * 新增保存用户转账明细
     */
    @RequiresPermissions("lb:detail:add")
    @Log(title = "用户转账明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TransferDetail transferDetail)
    {
        return toAjax(transferDetailService.insertTransferDetail(transferDetail));
    }

    /**
     * 修改用户转账明细
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TransferDetail transferDetail = transferDetailService.selectTransferDetailById(id);
        mmap.put("transferDetail", transferDetail);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户转账明细
     */
    @RequiresPermissions("lb:detail:edit")
    @Log(title = "用户转账明细", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TransferDetail transferDetail)
    {
        try {
            return toAjax(transferDetailService.updateTransferDetail(transferDetail));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 删除用户转账明细
     */
    @RequiresPermissions("lb:detail:remove")
    @Log(title = "用户转账明细", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(transferDetailService.deleteTransferDetailByIds(ids));
    }
}
