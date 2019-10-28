package com.ruoyi.project.lb.property.service;

import com.ruoyi.project.lb.property.domain.Property;
import java.util.List;

/**
 * 平台配置Service接口
 * 
 * @author u
 * @date 2019-10-10
 */
public interface IPropertyService 
{
    /**
     * 查询平台配置
     * 
     * @param id 平台配置ID
     * @return 平台配置
     */
    public Property selectPropertyById(Long id);

    /**
     * 查询平台配置列表
     * 
     * @param property 平台配置
     * @return 平台配置集合
     */
    public List<Property> selectPropertyList(Property property);

    /**
     * 新增平台配置
     * 
     * @param property 平台配置
     * @return 结果
     */
    public int insertProperty(Property property);

    /**
     * 修改平台配置
     * 
     * @param property 平台配置
     * @return 结果
     */
    public int updateProperty(Property property);

    /**
     * 批量删除平台配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePropertyByIds(String ids);

    /**
     * 删除平台配置信息
     * 
     * @param id 平台配置ID
     * @return 结果
     */
    public int deletePropertyById(Long id);
}
