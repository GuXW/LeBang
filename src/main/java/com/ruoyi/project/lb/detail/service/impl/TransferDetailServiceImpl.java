package com.ruoyi.project.lb.detail.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.content.TaskStatusEnum;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import com.ruoyi.project.system.user.service.UserServiceImpl;
import io.swagger.models.auth.In;
import org.junit.runners.model.FrameworkField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.detail.mapper.TransferDetailMapper;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import com.ruoyi.project.lb.detail.service.ITransferDetailService;
import com.ruoyi.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户转账明细Service业务层处理
 *
 * @author ${author}
 * @date 2019-10-08
 */
@Service
public class TransferDetailServiceImpl implements ITransferDetailService {
    @Autowired
    private TransferDetailMapper transferDetailMapper;

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Autowired
    private IUserService userService;

    /**
     * 查询用户转账明细
     *
     * @param id 用户转账明细ID
     * @return 用户转账明细
     */
    @Override
    public TransferDetail selectTransferDetailById(Long id) {
        return transferDetailMapper.selectTransferDetailById(id);
    }

    /**
     * 查询用户转账明细列表
     *
     * @param transferDetail 用户转账明细
     * @return 用户转账明细
     */
    @Override
    public List<TransferDetail> selectTransferDetailList(TransferDetail transferDetail) {
        return transferDetailMapper.selectTransferDetailList(transferDetail);
    }

    /**
     * 新增用户转账明细
     *
     * @param transferDetail 用户转账明细
     * @return 结果
     */
    @Override
    public int insertTransferDetail(TransferDetail transferDetail) {
        transferDetail.setCreateTime(DateUtils.getNowDate());
        return transferDetailMapper.insertTransferDetail(transferDetail);
    }

    /**
     * 修改用户转账明细
     *
     * @param transferDetail 用户转账明细
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateTransferDetail(TransferDetail transferDetail) throws Exception {
        if (transferDetail.getId() != null && transferDetail.getStatus() != null) {
            TransferDetail transferDetailTemp = this.selectTransferDetailById(transferDetail.getId());
            if (transferDetailTemp.getStatus() == 3) {
                throw new Exception("已确认，不可修改状态");
            }

            if (transferDetail.getStatus() == 3) {
                //修改为已确认时
                //更新收款用户余额
                SysUserSensitive toUser = sysUserSensitiveService.selectSysUserSensitiveByUserId(transferDetailTemp.getToId());
                BigDecimal userAmountBefore = new BigDecimal(toUser.getUserAmount() == null ? 0 : toUser.getUserAmount());
                BigDecimal amount = new BigDecimal(transferDetailTemp.getAmount() == null ? 0 : transferDetailTemp.getAmount());
                BigDecimal userAmountAfter = userAmountBefore.add(amount);
                SysUserSensitive toUserUpd = new SysUserSensitive();
                if (toUser.getInvitationId() != 1//判断是否销毁用户
                        && userAmountAfter.compareTo(new BigDecimal(toUser.getPredeterminedAmount() == null ? 0 : toUser.getPredeterminedAmount())) >= 0) {
                    //销毁账号
                    userService.deleteUserByIds(toUser.getUserId()+"");
                    //邀请码设置为空字符串 防止删除后还可以邀请
                    toUserUpd.setInvitationCode("");
                }

                toUserUpd.setId(toUser.getId());
                toUserUpd.setUpdateTime(DateUtils.getNowDate());
                toUserUpd.setUserAmount(userAmountAfter.doubleValue());
                sysUserSensitiveService.updateSysUserSensitive(toUserUpd);


                //升级用户
                SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(transferDetailTemp.getFromId());
                if (sysUserSensitive == null) {
                    throw new Exception("没有找到升级用户");
                }

                BigDecimal userLoveValBefore = new BigDecimal(sysUserSensitive.getUserLoveVal() == null ? 0 : sysUserSensitive.getUserLoveVal());

                //判断是否有几张未审核的订单明细
                TransferDetail transferDetail1 = new TransferDetail();
                transferDetail1.setFromId(sysUserSensitive.getUserId());
                transferDetail1.setNoStatus(3);
                SysUserSensitive sysUserSensitiveUpd = new SysUserSensitive();
                sysUserSensitiveUpd.setId(sysUserSensitive.getId());

                List<TransferDetail> transferDetails = this.selectTransferDetailList(transferDetail1);
                //判断是否可升级
                if (transferDetails != null && transferDetails.size() == 1 && transferDetailTemp.getToId().longValue() == transferDetails.get(0).getToId().longValue()) {
                    //升级
                    sysUserSensitiveUpd.setUserLevel(sysUserSensitive.getUserLevel() + 1);
                    if (sysUserSensitiveUpd.getUserLevel()==1){//设置等级为1时设置激活时间
                        //设置激活时间
                        sysUserSensitiveUpd.setActivationTime(DateUtils.getNowDate());
                    }
                }
                //设置爱心数
                sysUserSensitiveUpd.setUserLoveVal(userLoveValBefore.add(new BigDecimal(transferDetailTemp.getAmount() == null ? 0 : transferDetailTemp.getAmount())).doubleValue());
                sysUserSensitiveService.updateSysUserSensitive(sysUserSensitiveUpd);

            }
        }


        transferDetail.setUpdateTime(DateUtils.getNowDate());
        return transferDetailMapper.updateTransferDetail(transferDetail);
    }

    /**
     * 删除用户转账明细对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTransferDetailByIds(String ids) {
        return transferDetailMapper.deleteTransferDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户转账明细信息
     *
     * @param id 用户转账明细ID
     * @return 结果
     */
    public int deleteTransferDetailById(Long id) {
        return transferDetailMapper.deleteTransferDetailById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult confirmUpLevel(Long userId, Long id, Integer status) throws Exception {
        //获取当前转账明细
        TransferDetail transferDetail = this.selectTransferDetailById(id);
        if (transferDetail == null) {
            return AjaxResult.error("获取不到转账明细");
        }

        if (userId.longValue() != transferDetail.getToId().longValue()) {
            return AjaxResult.error("非法确认");
        }
        TransferDetail transferDetailUpd = new TransferDetail();
        transferDetailUpd.setId(id);
        transferDetailUpd.setStatus(status);
        this.updateTransferDetail(transferDetailUpd);

//        if (status==3){
//
//            //确认状态下修改当前用户
//            //获取确认人的用户信息
//            SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(transferDetail.getToId());
//            if (sysUserSensitive == null){
//                throw  new Exception("获取不到收款用户信息");
//            }
//            if (sysUserSensitive.getInvitationId().longValue()!=1l){//上级不为1时则说明他不是管理员用户 需要交易预定金额和实际收入是否到了删除改用户时刻
//                //用户金额
//                BigDecimal userAmountDecimal=new BigDecimal( sysUserSensitive.getUserAmount());
//                //转账金额
//                BigDecimal amountDecimal = new BigDecimal(transferDetail.getAmount());
//                //转账后用户金额
//                BigDecimal afterAmount = userAmountDecimal.add(amountDecimal);
//                if ( afterAmount.doubleValue() >= sysUserSensitive.getPredeterminedAmount()){
//                    //销毁账户
//                    userService.deleteUserById(sysUserSensitive.getUserId());
//                }
//                //更新账户余额
//                SysUserSensitive sysUserSensitiveUpd=new SysUserSensitive();
//                sysUserSensitiveUpd.setId(sysUserSensitive.getId());
//                sysUserSensitiveUpd.setUid(userId);
//                sysUserSensitiveUpd.setUpdateTime(DateUtils.getNowDate());
//                sysUserSensitiveUpd.setUserAmount(afterAmount.doubleValue());
//                sysUserSensitiveService.updateSysUserSensitive(sysUserSensitiveUpd);
//
//            }
//
//            //确认状态时判断用户是否可升级
//            //查询
//            TransferDetail transferDetailTemp=new TransferDetail();
//            transferDetailTemp.setFromId(transferDetail.getFromId());
//            transferDetailTemp.setUpLevel(transferDetail.getUpLevel());
//            //获取当前等级所有转账记录
//            List<TransferDetail> transferDetails = this.selectTransferDetailList(transferDetailTemp);
//            boolean flag=true;//是否可以升级标识
//            BigDecimal loveVal=new BigDecimal(0);
//            for (TransferDetail transferDetail1:transferDetails){
//                if (transferDetail1.getStatus()!=3){
//                    flag=false;
//                }else{
//                    //否则增加爱心值
//                    loveVal.add(BigDecimal.valueOf(transferDetail1.getAmount()==null?0:transferDetail1.getAmount()));
//                }
//            }
//
//            if (flag){
//                SysUserSensitive sysUserSensitiveTemp = sysUserSensitiveService.selectSysUserSensitiveByUserId(transferDetail.getFromId());
//                if (sysUserSensitiveTemp==null){
//                    throw  new Exception("获取不到用户信息");
//                }
//                //用户升级
//                SysUserSensitive sysUserSensitiveUpd= new SysUserSensitive();
//                sysUserSensitiveUpd.setId(sysUserSensitiveTemp.getId());
//                sysUserSensitiveUpd.setUserLevel(sysUserSensitiveTemp.getUserLevel()+1);//升级
//                //增加爱心值
//                sysUserSensitiveUpd.setUserLoveVal(BigDecimal.valueOf(sysUserSensitiveTemp.getUserLoveVal()==null?0.00:sysUserSensitiveTemp.getUserLoveVal()).add(loveVal).doubleValue());
//                sysUserSensitiveService.updateSysUserSensitive(sysUserSensitiveUpd);
//            }
//        }
        return AjaxResult.success("确认成功");
    }

    @Override
    public AjaxResult taskDetail(Long userId) {
        if (userId == null) {
            return AjaxResult.error("获取不到用户id");
        }
        //获取用户信息
        SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(userId);

        if (sysUserSensitive == null) {
            return AjaxResult.error("获取不到用户信息");
        }

        //查询收款记录
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setToId(userId);
        transferDetail.setStatus(TaskStatusEnum.CONFIRM.getValue());
        List<TransferDetail> transferDetails = transferDetailMapper.selectTransferDetailList(transferDetail);

        //设置层数对应获取金额
        Map<String, Object> map = new HashMap<>();
        if (transferDetails != null) {
            for (TransferDetail transferDetailTemp :
                    transferDetails) {
                if (map.get(transferDetailTemp.getUpLevel() + "") == null) {
                    map.put(transferDetailTemp.getUpLevel() + "", transferDetailTemp.getAmount());
                } else {
                    BigDecimal amount = BigDecimal.valueOf(Double.valueOf(map.get(transferDetailTemp.getUpLevel() + "").toString()))
                            .add(BigDecimal.valueOf(transferDetailTemp.getAmount() != null ? transferDetailTemp.getAmount() : 0));
                    map.put(transferDetailTemp.getUpLevel() + "", amount.doubleValue());
                }
            }
        }


        List<Object> list = new LinkedList();
        for (int i = 1; i < 11; i++) {
            if (map.get(i + "") != null) {
                list.add(map.get(i + ""));
            } else {
                list.add(0);
            }
        }

        //设置返回数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("plan", list);
        resultMap.put("totalAmount", sysUserSensitive.getPredeterminedAmount());
        resultMap.put("taskType", sysUserSensitive.getTaskType());
        resultMap.put("userLevel", sysUserSensitive.getUserLevel());
        return AjaxResult.success(resultMap);
    }
}
