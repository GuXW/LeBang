package com.ruoyi.project.lb.sensitive.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.sensitive.domain.EachTeam;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import java.util.List;

/**
 * 用户敏感数据Service接口
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public interface ISysUserSensitiveService 
{
    /**
     * 查询用户敏感数据
     * 
     * @param id 用户敏感数据ID
     * @return 用户敏感数据
     */
    public SysUserSensitive selectSysUserSensitiveById(Long id);

    /**
     * 查询用户敏感数据列表
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 用户敏感数据集合
     */
    public List<SysUserSensitive> selectSysUserSensitiveList(SysUserSensitive sysUserSensitive);

    /**
     * 新增用户敏感数据
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 结果
     */
    public int insertSysUserSensitive(SysUserSensitive sysUserSensitive);

    /**
     * 修改用户敏感数据
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 结果
     */
    public int updateSysUserSensitive(SysUserSensitive sysUserSensitive);

    /**
     * 批量删除用户敏感数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserSensitiveByIds(String ids);

    /**
     * 删除用户敏感数据信息
     * 
     * @param id 用户敏感数据ID
     * @return 结果
     */
    public int deleteSysUserSensitiveById(Long id);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    SysUserSensitive selectSysUserSensitiveByUserId(Long id);

    /**
     * 创建任务
     * @param sysUserSensitive
     * @return
     */
    AjaxResult createTask(SysUserSensitive sysUserSensitive)  throws Exception ;

    /**
     * 查询我的团队
     * @return
     */
    List<EachTeam> queryMyTeam(Long userId) throws Exception;

    /**
     * 升级请求
     * @param userId
     * @return
     */
    AjaxResult upgrade(Long userId) throws Exception ;

    List<String> getNewActivationer() throws Exception ;
}
