package com.ruoyi.project.lb.detail.mapper;

import com.ruoyi.project.lb.detail.domain.TransferDetail;
import java.util.List;

/**
 * 用户转账明细Mapper接口
 * 
 * @author ${author}
 * @date 2019-10-08
 */
public interface TransferDetailMapper 
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
    public int updateTransferDetail(TransferDetail transferDetail);

    /**
     * 删除用户转账明细
     * 
     * @param id 用户转账明细ID
     * @return 结果
     */
    public int deleteTransferDetailById(Long id);

    /**
     * 批量删除用户转账明细
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTransferDetailByIds(String[] ids);
}
