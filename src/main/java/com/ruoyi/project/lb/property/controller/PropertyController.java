package com.ruoyi.project.lb.property.controller;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
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
import com.ruoyi.project.lb.property.domain.Property;
import com.ruoyi.project.lb.property.service.IPropertyService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 平台配置Controller
 * 
 * @author u
 * @date 2019-10-10
 */
@Controller
@RequestMapping("/lb/property")
public class PropertyController extends BaseController
{
    private String prefix = "lb/property";

    @Autowired
    private IPropertyService propertyService;

    @RequiresPermissions("lb:property:view")
    @GetMapping()
    public String property()
    {
        return prefix + "/property";
    }

    /**
     * 查询平台配置列表
     */
    @RequiresPermissions("lb:property:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Property property)
    {
        startPage();
        List<Property> list = propertyService.selectPropertyList(property);
        return getDataTable(list);
    }

    /**
     * 导出平台配置列表
     */
    @RequiresPermissions("lb:property:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Property property)
    {
        List<Property> list = propertyService.selectPropertyList(property);
        ExcelUtil<Property> util = new ExcelUtil<Property>(Property.class);
        return util.exportExcel(list, "property");
    }

    /**
     * 新增平台配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存平台配置
     */
    @RequiresPermissions("lb:property:add")
    @Log(title = "平台配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Property property)
    {
        property.setCid(ShiroUtils.getUserId());
        property.setCreateTime(DateUtils.getNowDate());
        return toAjax(propertyService.insertProperty(property));
    }

    /**
     * 修改平台配置
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Property property = propertyService.selectPropertyById(id);
        mmap.put("property", property);
        return prefix + "/edit";
    }

    /**
     * 修改保存平台配置
     */
    @RequiresPermissions("lb:property:edit")
    @Log(title = "平台配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Property property)
    {
        property.setUid(ShiroUtils.getUserId());
        property.setUpdateTime(DateUtils.getNowDate());
        return toAjax(propertyService.updateProperty(property));
    }

    /**
     * 删除平台配置
     */
    @RequiresPermissions("lb:property:remove")
    @Log(title = "平台配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(propertyService.deletePropertyByIds(ids));
    }
}
