package com.ruoyi.project.lb.chart.service;

import com.ruoyi.project.lb.chart.domain.RotationChart;
import java.util.List;

/**
 * 轮播图Service接口
 * 
 * @author ${author}
 * @date 2019-10-10
 */
public interface IRotationChartService 
{
    /**
     * 查询轮播图
     * 
     * @param id 轮播图ID
     * @return 轮播图
     */
    public RotationChart selectRotationChartById(Long id);

    /**
     * 查询轮播图列表
     * 
     * @param rotationChart 轮播图
     * @return 轮播图集合
     */
    public List<RotationChart> selectRotationChartList(RotationChart rotationChart);

    /**
     * 新增轮播图
     * 
     * @param rotationChart 轮播图
     * @return 结果
     */
    public int insertRotationChart(RotationChart rotationChart);

    /**
     * 修改轮播图
     * 
     * @param rotationChart 轮播图
     * @return 结果
     */
    public int updateRotationChart(RotationChart rotationChart);

    /**
     * 批量删除轮播图
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRotationChartByIds(String ids);

    /**
     * 删除轮播图信息
     * 
     * @param id 轮播图ID
     * @return 结果
     */
    public int deleteRotationChartById(Long id);
}
