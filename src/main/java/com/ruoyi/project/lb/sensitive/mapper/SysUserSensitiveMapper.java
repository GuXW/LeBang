package com.ruoyi.project.lb.sensitive.mapper;

import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import java.util.List;

/**
 * 用户敏感数据Mapper接口
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public interface SysUserSensitiveMapper 
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
     * 删除用户敏感数据
     * 
     * @param id 用户敏感数据ID
     * @return 结果
     */
    public int deleteSysUserSensitiveById(Long id);

    /**
     * 批量删除用户敏感数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserSensitiveByIds(String[] ids);
    /**
     * 查询用户敏感数据
     *
     * @param id 用户ID
     * @return 用户敏感数据
     */
    SysUserSensitive selectSysUserSensitiveByUserId(Long id);

    /**
     * 获取所有下级用户信息
     * @param userId
     * @return
     */
    List<SysUserSensitive> queryAllSubUserByInvitation(Long userId);

    /**
     * 获取所有上级
     */
    SysUserSensitive queryAllInvitationByInvitationId(Long invitationId);

    /**
     * 获取最新的十个激活人手机号
     * @return
     */
    List<String> getNewActivationer();
}
