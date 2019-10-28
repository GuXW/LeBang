package com.ruoyi.project.lb.property.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.property.mapper.PropertyMapper;
import com.ruoyi.project.lb.property.domain.Property;
import com.ruoyi.project.lb.property.service.IPropertyService;
import com.ruoyi.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 平台配置Service业务层处理
 * 
 * @author u
 * @date 2019-10-10
 */
@Service
public class PropertyServiceImpl implements IPropertyService 
{
    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void initProperty(){
        Property property = new Property();
        property.setStatus(1);
        List<Property> properties = this.selectPropertyList(property);
        for (Property propertyTemp:
             properties) {
            redisUtils.hset(RedisUtils.PROPERTY_NAME,propertyTemp.getId()+"",propertyTemp.getPropertyName());
            redisUtils.hset(RedisUtils.PROPERTY,propertyTemp.getPropertyName(),propertyTemp.getPropertyValue());
        }
    }

    /**
     * 查询平台配置
     * 
     * @param id 平台配置ID
     * @return 平台配置
     */
    @Override
    public Property selectPropertyById(Long id)
    {
        return propertyMapper.selectPropertyById(id);
    }

    /**
     * 查询平台配置列表
     * 
     * @param property 平台配置
     * @return 平台配置
     */
    @Override
    public List<Property> selectPropertyList(Property property)
    {
        return propertyMapper.selectPropertyList(property);
    }

    /**
     * 新增平台配置
     * 
     * @param property 平台配置
     * @return 结果
     */
    @Override
    public int insertProperty(Property property)
    {
        property.setCreateTime(DateUtils.getNowDate());
        int count = propertyMapper.insertProperty(property);
        if (count>0){
            redisUtils.hset(RedisUtils.PROPERTY_NAME,property.getId()+"",property.getPropertyName());
            redisUtils.hset(RedisUtils.PROPERTY,property.getPropertyName(),property.getPropertyValue());
        }
        return count;
    }

    /**
     * 修改平台配置
     * 
     * @param property 平台配置
     * @return 结果
     */
    @Override
    public int updateProperty(Property property)
    {
        property.setUpdateTime(DateUtils.getNowDate());
        int count = propertyMapper.updateProperty(property);
        if (count>0){
            redisUtils.hset(RedisUtils.PROPERTY_NAME,property.getId()+"",property.getPropertyName());
            redisUtils.hset(RedisUtils.PROPERTY,property.getPropertyName(),property.getPropertyValue());
        }
        return count;
    }

    /**
     * 删除平台配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePropertyByIds(String ids)
    {
        int count = propertyMapper.deletePropertyByIds(Convert.toStrArray(ids));
        if (count>0){
            String[] id = ids.split(",");
            for (String idTemp:id){
                Object hget = redisUtils.hget(RedisUtils.PROPERTY_NAME, idTemp);
                if (hget!=null){
                    redisUtils.hdel(RedisUtils.PROPERTY, hget.toString());
                    redisUtils.hdel(RedisUtils.PROPERTY_NAME, idTemp);
                }
            }
        }
        return count;
    }

    /**
     * 删除平台配置信息
     * 
     * @param id 平台配置ID
     * @return 结果
     */
    public int deletePropertyById(Long id)
    {
        int count =  propertyMapper.deletePropertyById(id);
        if (count>0){
            Object hget = redisUtils.hget(RedisUtils.PROPERTY_NAME, id+"");
            if (hget!=null){
                redisUtils.hdel(RedisUtils.PROPERTY, hget.toString());
                redisUtils.hdel(RedisUtils.PROPERTY_NAME, id+"");
            }
        }
        return count;
    }
}
