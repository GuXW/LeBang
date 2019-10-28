package com.ruoyi.project.lb.chart.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.chart.mapper.RotationChartMapper;
import com.ruoyi.project.lb.chart.domain.RotationChart;
import com.ruoyi.project.lb.chart.service.IRotationChartService;
import com.ruoyi.common.utils.text.Convert;

/**
 * 轮播图Service业务层处理
 * 
 * @author ${author}
 * @date 2019-10-10
 */
@Service
public class RotationChartServiceImpl implements IRotationChartService 
{
    @Autowired
    private RotationChartMapper rotationChartMapper;

    /**
     * 查询轮播图
     * 
     * @param id 轮播图ID
     * @return 轮播图
     */
    @Override
    public RotationChart selectRotationChartById(Long id)
    {
        return rotationChartMapper.selectRotationChartById(id);
    }

    /**
     * 查询轮播图列表
     * 
     * @param rotationChart 轮播图
     * @return 轮播图
     */
    @Override
    public List<RotationChart> selectRotationChartList(RotationChart rotationChart)
    {
        return rotationChartMapper.selectRotationChartList(rotationChart);
    }

    /**
     * 新增轮播图
     * 
     * @param rotationChart 轮播图
     * @return 结果
     */
    @Override
    public int insertRotationChart(RotationChart rotationChart)
    {
        rotationChart.setCreateTime(DateUtils.getNowDate());
        return rotationChartMapper.insertRotationChart(rotationChart);
    }

    /**
     * 修改轮播图
     * 
     * @param rotationChart 轮播图
     * @return 结果
     */
    @Override
    public int updateRotationChart(RotationChart rotationChart)
    {
        rotationChart.setUpdateTime(DateUtils.getNowDate());
        return rotationChartMapper.updateRotationChart(rotationChart);
    }

    /**
     * 删除轮播图对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteRotationChartByIds(String ids)
    {
        return rotationChartMapper.deleteRotationChartByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除轮播图信息
     * 
     * @param id 轮播图ID
     * @return 结果
     */
    public int deleteRotationChartById(Long id)
    {
        return rotationChartMapper.deleteRotationChartById(id);
    }
}
