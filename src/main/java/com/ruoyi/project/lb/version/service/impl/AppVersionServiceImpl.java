package com.ruoyi.project.lb.version.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.version.mapper.AppVersionMapper;
import com.ruoyi.project.lb.version.domain.AppVersion;
import com.ruoyi.project.lb.version.service.IAppVersionService;
import com.ruoyi.common.utils.text.Convert;

/**
 * app版本号管理Service业务层处理
 * 
 * @author ${author}
 * @date 2019-10-23
 */
@Service
public class AppVersionServiceImpl implements IAppVersionService 
{
    @Autowired
    private AppVersionMapper appVersionMapper;

    /**
     * 查询app版本号管理
     * 
     * @param id app版本号管理ID
     * @return app版本号管理
     */
    @Override
    public AppVersion selectAppVersionById(Long id)
    {
        return appVersionMapper.selectAppVersionById(id);
    }

    /**
     * 查询app版本号管理列表
     * 
     * @param appVersion app版本号管理
     * @return app版本号管理
     */
    @Override
    public List<AppVersion> selectAppVersionList(AppVersion appVersion)
    {
        return appVersionMapper.selectAppVersionList(appVersion);
    }

    /**
     * 新增app版本号管理
     * 
     * @param appVersion app版本号管理
     * @return 结果
     */
    @Override
    public int insertAppVersion(AppVersion appVersion)
    {
        appVersion.setCreateTime(DateUtils.getNowDate());
        return appVersionMapper.insertAppVersion(appVersion);
    }

    /**
     * 修改app版本号管理
     * 
     * @param appVersion app版本号管理
     * @return 结果
     */
    @Override
    public int updateAppVersion(AppVersion appVersion)
    {
        appVersion.setUpdateTime(DateUtils.getNowDate());
        return appVersionMapper.updateAppVersion(appVersion);
    }

    /**
     * 删除app版本号管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAppVersionByIds(String ids)
    {
        return appVersionMapper.deleteAppVersionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除app版本号管理信息
     * 
     * @param id app版本号管理ID
     * @return 结果
     */
    public int deleteAppVersionById(Long id)
    {
        return appVersionMapper.deleteAppVersionById(id);
    }

    @Override
    public AppVersion selectNewAppVersionByType(AppVersion appVersion) {
        return  appVersionMapper.selectNewAppVersionByType(appVersion);
    }
}
