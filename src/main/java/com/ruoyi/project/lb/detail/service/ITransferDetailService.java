package com.ruoyi.project.lb.detail.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import java.util.List;

/**
 * 用户转账明细Service接口
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public interface ITransferDetailService 
{
    /**
     * 查询用户转账明细
     * 
     * @param id 用户转账明细ID
     * @return 用户转账明细
     */
    public TransferDetail selectTransferDetailById(Long id);

    /**
     * 查询用户转账明细列表
     * 
     * @param transferDetail 用户转账明细
     * @return 用户转账明细集合
     */
    public List<TransferDetail> selectTransferDetailList(TransferDetail transferDetail);

    /**
     * 新增用户转账明细
     * 
     * @param transferDetail 用户转账明细
     * @return 结果
     */
    public int insertTransferDetail(TransferDetail transferDetail);

    /**
     * 修改用户转账明细
     * 
     * @param transferDetail 用户转账明细
     * @return 结果
     */
    public int updateTransferDetail(TransferDetail transferDetail)  throws Exception;

    /**
     * 批量删除用户转账明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTransferDetailByIds(String ids);

    /**
     * 删除用户转账明细信息
     * 
     * @param id 用户转账明细ID
     * @return 结果
     */
    public int deleteTransferDetailById(Long id);

    AjaxResult confirmUpLevel(Long userId, Long id,Integer status)  throws Exception;

    AjaxResult taskDetail(Long userId);
}
