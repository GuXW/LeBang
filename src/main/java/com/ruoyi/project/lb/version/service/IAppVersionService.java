package com.ruoyi.project.lb.version.service;

import com.ruoyi.project.lb.version.domain.AppVersion;
import java.util.List;

/**
 * app版本号管理Service接口
 * 
 * @author ${author}
 * @date 2019-10-23
 */
public interface IAppVersionService 
{
    /**
     * 查询app版本号管理
     * 
     * @param id app版本号管理ID
     * @return app版本号管理
     */
    public AppVersion selectAppVersionById(Long id);

    /**
     * 查询app版本号管理列表
     * 
     * @param appVersion app版本号管理
     * @return app版本号管理集合
     */
    public List<AppVersion> selectAppVersionList(AppVersion appVersion);

    /**
     * 新增app版本号管理
     * 
     * @param appVersion app版本号管理
     * @return 结果
     */
    public int insertAppVersion(AppVersion appVersion);

    /**
     * 修改app版本号管理
     * 
     * @param appVersion app版本号管理
     * @return 结果
     */
    public int updateAppVersion(AppVersion appVersion);

    /**
     * 批量删除app版本号管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppVersionByIds(String ids);

    /**
     * 删除app版本号管理信息
     * 
     * @param id app版本号管理ID
     * @return 结果
     */
    public int deleteAppVersionById(Long id);

    /**
     * 获取最新的app配置
     * @return
     */
    AppVersion selectNewAppVersionByType(AppVersion appVersion);
}
