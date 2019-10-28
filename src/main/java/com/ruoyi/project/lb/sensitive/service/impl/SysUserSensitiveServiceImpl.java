package com.ruoyi.project.lb.sensitive.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.content.TaskStatusEnum;
import com.ruoyi.project.content.UserConfirmEnum;
import com.ruoyi.project.lb.detail.controller.TransferDetailController;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import com.ruoyi.project.lb.detail.service.ITransferDetailService;
import com.ruoyi.project.lb.sensitive.domain.EachTeam;
import com.ruoyi.project.utils.RedisUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import com.ruoyi.project.lb.sensitive.mapper.SysUserSensitiveMapper;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.common.utils.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户敏感数据Service业务层处理
 * 
 * @author ${author}
 * @date 2019-10-08
 */
@Service
public class SysUserSensitiveServiceImpl implements ISysUserSensitiveService 
{
    @Autowired
    private SysUserSensitiveMapper sysUserSensitiveMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ITransferDetailService transferDetailService;


    /**
     * 查询用户敏感数据
     * 
     * @param id 用户敏感数据ID
     * @return 用户敏感数据
     */
    @Override
    public SysUserSensitive selectSysUserSensitiveById(Long id)
    {
        return sysUserSensitiveMapper.selectSysUserSensitiveById(id);
    }

    /**
     * 查询用户敏感数据列表
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 用户敏感数据
     */
    @Override
    public List<SysUserSensitive> selectSysUserSensitiveList(SysUserSensitive sysUserSensitive)
    {
        return sysUserSensitiveMapper.selectSysUserSensitiveList(sysUserSensitive);
    }

    /**
     * 新增用户敏感数据
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 结果
     */
    @Override
    public int insertSysUserSensitive(SysUserSensitive sysUserSensitive)
    {
        sysUserSensitive.setCreateTime(DateUtils.getNowDate());
        return sysUserSensitiveMapper.insertSysUserSensitive(sysUserSensitive);
    }

    /**
     * 修改用户敏感数据
     * 
     * @param sysUserSensitive 用户敏感数据
     * @return 结果
     */
    @Override
    public int updateSysUserSensitive(SysUserSensitive sysUserSensitive)
    {
        sysUserSensitive.setUpdateTime(DateUtils.getNowDate());
        return sysUserSensitiveMapper.updateSysUserSensitive(sysUserSensitive);
    }

    /**
     * 删除用户敏感数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysUserSensitiveByIds(String ids)
    {
        return sysUserSensitiveMapper.deleteSysUserSensitiveByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户敏感数据信息
     * 
     * @param id 用户敏感数据ID
     * @return 结果
     */
    public int deleteSysUserSensitiveById(Long id)
    {
        return sysUserSensitiveMapper.deleteSysUserSensitiveById(id);
    }

    @Override
    public SysUserSensitive selectSysUserSensitiveByUserId(Long id) {
        return sysUserSensitiveMapper.selectSysUserSensitiveByUserId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult createTask(SysUserSensitive sysUserSensitive) throws Exception {
        if (sysUserSensitive.getUserId() == null){
            return AjaxResult.error("用户id不能为空");
        }

        if (sysUserSensitive.getPredeterminedAmount() == null){
            return AjaxResult.error("金额不能为空");
        }

        if (sysUserSensitive.getUserCredentials() == null){
            return AjaxResult.error("任务凭证图不能为空");
        }

        if (sysUserSensitive.getTaskType() == null){
            return AjaxResult.error("任务类型不能为空");
        }

        SysUserSensitive sysUserSensitiveTemp = this.selectSysUserSensitiveByUserId(sysUserSensitive.getUserId());
        if (sysUserSensitiveTemp == null){
            return AjaxResult.error("获取不到用户信息");
        }

        if (sysUserSensitiveTemp.getInvitationId().longValue()==1l){
            return AjaxResult.error("平台账户不需要创建计划");
        }

        if (!sysUserSensitiveTemp.getConfirmStatus().equals(UserConfirmEnum.check_success.getValue()+"")){
            return AjaxResult.error("当前用户未进行实名认证,不可创建");
        }

        if (StringUtils.isBlank(sysUserSensitiveTemp.getReceivingAccount())){
            return AjaxResult.error("请先上传收款方式");
        }

        if (sysUserSensitiveTemp.getUserCredentials()!=null){
            return AjaxResult.error("已有任务，不可再次创建");
        }

        SysUserSensitive sysUserSensitiveInsert=new SysUserSensitive();
        sysUserSensitiveInsert.setId(sysUserSensitiveTemp.getId());
        sysUserSensitiveInsert.setPredeterminedAmount(sysUserSensitive.getPredeterminedAmount());
        sysUserSensitiveInsert.setUserCredentials(sysUserSensitive.getUserCredentials());
        sysUserSensitiveInsert.setTaskType(sysUserSensitive.getTaskType());
        //更新信息
        int count = this.updateSysUserSensitive(sysUserSensitiveInsert);

        if (count>0) {
            //更新成功 创建交易流水
            //获取交易配置
            Object firstActivation = redisUtils.hget(RedisUtils.PROPERTY, RedisUtils.FIRST_ACTIVATION);
            Object costPerLevel = redisUtils.hget(RedisUtils.PROPERTY, RedisUtils.COST_PER_LEVEL);
            //判断配置是否为空
            if (firstActivation!=null&&costPerLevel!=null){
                //需给第十级打款
                BigDecimal firstActivationDecimal = BigDecimal.valueOf(Double.valueOf(firstActivation.toString()));
                //需给上级打款金额
                BigDecimal costPerLevelDecimal = BigDecimal.valueOf(Double.valueOf(costPerLevel.toString()));
                //获取上级用户信息
                SysUserSensitive invitationUser = this.selectSysUserSensitiveByUserId(sysUserSensitiveTemp.getInvitationId());
                if (invitationUser == null){
                    insertTransferDetail(sysUserSensitiveTemp, costPerLevelDecimal, 1l);
                }else{
                    if (Integer.valueOf(invitationUser.getDelFlag())==0 && invitationUser.getUserLevel()>=1) {
                        //设置给邀请人打款
                        insertTransferDetail(sysUserSensitiveTemp, costPerLevelDecimal, sysUserSensitiveTemp.getInvitationId());
                    }else{
                        //获取当前用户所有上级
                        SysUserSensitive inviter = sysUserSensitiveMapper.queryAllInvitationByInvitationId(sysUserSensitiveTemp.getInvitationId());
                        //查看当前用户上升一级需给哪个用户付款
                        //返回本应付款用户
                        SysUserSensitive inviterByLevel = getInviterByLevel(sysUserSensitiveTemp.getUserLevel()+1, inviter);
                        //判断用户是否符合条件
                        SysUserSensitive checkInviterLevel = getCheckInviterLevel(sysUserSensitiveTemp.getUserLevel() + 1, inviterByLevel);

                        //需给上级打款金额
                        insertTransferDetail(sysUserSensitiveTemp, costPerLevelDecimal, checkInviterLevel.getUserId());
                    }
                }
                //设置给最近的第十级用户打款
                if (invitationUser.getUserLevel()==10){
                    //新增打款明细
                    insertTransferDetail(sysUserSensitiveTemp, firstActivationDecimal, sysUserSensitiveTemp.getInvitationId());
                }else{
                    //新增打款明细
                    insertTransferDetail(sysUserSensitiveTemp, firstActivationDecimal, getTheLatestTenUserId(sysUserSensitiveTemp.getInvitationId()));
                }

            }else{
                //有一个为空就报错
                throw new Exception("配置缺失");
            }

        }
        return AjaxResult.success(count);
    }
    @Override
    public List<EachTeam> queryMyTeam(Long userId) throws Exception {
        List<EachTeam> listResultTemp=new ArrayList<>();
        List<EachTeam> listResult=new ArrayList<>();
        //查询我的第n层用户人数
        List<SysUserSensitive> sysUserSensitives = sysUserSensitiveMapper.queryAllSubUserByInvitation(userId);
        getSubUserNumber(sysUserSensitives,1,userId,listResultTemp);
        listResultTemp.sort(Comparator.comparingInt(EachTeam::getLevel));
        for(int i=1;i<11;i++){
            Integer leakOrderCount=0;//漏单数
            Integer ReceivedOrderCount=0;//收到订单单数
            Integer number=0; //人数
            for (EachTeam eachTeam:listResultTemp){
                if (i==eachTeam.getLevel()){
                    leakOrderCount+=eachTeam.getLeakOrderNum();
                    ReceivedOrderCount+=eachTeam.getReceivedOrderNum();
                    number+=eachTeam.getNumber();
                }
            }
            EachTeam eachTeam=new EachTeam();
            eachTeam.setLevel(i);
            eachTeam.setLeakOrderNum(leakOrderCount);
            eachTeam.setReceivedOrderNum(ReceivedOrderCount);
            eachTeam.setNumber(number);
            listResult.add(eachTeam);
        }
        return listResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult upgrade(Long userId) throws Exception  {
    //******************************校验开始******************************
        //获取当前用户信息
        SysUserSensitive sysUserSensitive = selectSysUserSensitiveByUserId(userId);
        //判断是否能获取到用户信息
        if (sysUserSensitive==null){
            //获取不到则报错
            return AjaxResult.error("用户信息不能为空");
        }

        //判断当前用户是否有未确认的转账记录
        //获取用户未转账记录
        TransferDetail transferDetail=new TransferDetail();
        transferDetail.setFromId(userId);
        transferDetail.setNoStatus(3);
        //查找有多少未确认的订单 包含未付款的订单
        List<TransferDetail> transferDetails = transferDetailService.selectTransferDetailList(transferDetail);
        if (transferDetails!=null&&transferDetails.size()>0){
            return AjaxResult.error("你有未确认或未付款的订单,请先进行付款或联系管理员进行确认");
        }

        //判断当前用户等级是否为0 为0则不能升级 需要先创建
        if (sysUserSensitive.getUserLevel()==0){
            return AjaxResult.error("需要先创建创业计划或者是还款计划");
        }

        //判断当前用户等级是否已到十级 十级后不能在升级
        if (sysUserSensitive.getUserLevel()==10){
            return AjaxResult.error("已经到达了最顶级,无需在升级");
        }
        Object costPerLevel = redisUtils.hget(RedisUtils.PROPERTY, RedisUtils.COST_PER_LEVEL);
        if (costPerLevel==null)  return AjaxResult.error("获取不到升级所需打款金额配置");
        //******************************校验结束******************************

        //******************************升级开始*****************************
        //获取当前用户所有上级
        SysUserSensitive inviter = sysUserSensitiveMapper.queryAllInvitationByInvitationId(sysUserSensitive.getInvitationId());
        //查看当前用户上升一级需给哪个用户付款
        //返回本应付款用户
        SysUserSensitive inviterByLevel = getInviterByLevel(sysUserSensitive.getUserLevel()+1, inviter);
        //判断用户是否符合条件
        SysUserSensitive checkInviterLevel = getCheckInviterLevel(sysUserSensitive.getUserLevel() + 1, inviterByLevel);

        //需给上级打款金额
        BigDecimal costPerLevelDecimal = BigDecimal.valueOf(Double.valueOf(costPerLevel.toString()));
        //通过用户id进行
        insertTransferDetail(sysUserSensitive, costPerLevelDecimal, checkInviterLevel.getUserId());
        //******************************升级结束*****************************
        return AjaxResult.success();
    }

    @Override
    public List<String> getNewActivationer() throws Exception  {
        List<String> newActivationer = sysUserSensitiveMapper.getNewActivationer();
        List<String> mobileList=new ArrayList<>();
        if (newActivationer!=null && newActivationer.size()>0){
            for (String strMobile:
                 newActivationer) {
                if (strMobile.length()>8){
                    StringBuffer replaceMobile = new StringBuffer(strMobile).replace(3, 7, "****");
                    mobileList.add(replaceMobile.toString());
                }
            }
        }
        return mobileList;
    }

    private SysUserSensitive getCheckInviterLevel(int level,SysUserSensitive inviterByLevel) {
        //判断该用户是否到达该等级 到达则可以给该用户打款
        if (inviterByLevel.getUserLevel()>=level || inviterByLevel.getInvitationId() == 1){
            if (Integer.valueOf(inviterByLevel.getDelFlag()).intValue()==0){
                return inviterByLevel;
            }else{
                return getCheckInviterLevel(level,inviterByLevel.getInviter());
            }
        }else{
            return getCheckInviterLevel(level,inviterByLevel.getInviter());
        }
    }

    /**
     * 根据等级查看需给哪层用户进行打款的用户信息
     * @param level
     * @return
     */
    private SysUserSensitive getInviterByLevel(int level,SysUserSensitive inviter){
        SysUserSensitive inviterTemp = null;
        for (int i=0;i<level;i++){
            if (inviter.getInvitationId().longValue()!=1){
                inviterTemp = inviter.getInviter();
            }else{
                inviterTemp = inviter;
                break;
            }
        }
        return inviterTemp;
    }

    private void getSubUserNumber(List<SysUserSensitive> sysUserSensitives,int level,Long currentUserId,List<EachTeam> listResult){
        if(level<=10){//到第n级结束统计
            EachTeam eachTeam=new EachTeam();
            eachTeam.setNumber(sysUserSensitives.size());
            eachTeam.setLevel(level);
            Integer leakOrderCount=0;//漏单数
            Integer ReceivedOrderCount=0;//收到订单单数

            for (SysUserSensitive sysUserSensitive:
                sysUserSensitives) {
                //查询当前用户给我转账明细
                TransferDetail transferDetail=new TransferDetail();
                transferDetail.setFromId(sysUserSensitive.getUserId());
                transferDetail.setToId(currentUserId);
                //第几层升第几级
                transferDetail.setUpLevel(eachTeam.getLevel());
                List<TransferDetail> transferDetails = transferDetailService.selectTransferDetailList(transferDetail);
                //统计收到订单数
                ReceivedOrderCount+=(transferDetails!=null?transferDetails.size():0);
                //判断当前用户是否该给我打款（升级漏单） (当前用户等级是否大于等级用户层级数)
                if (sysUserSensitive.getUserLevel().intValue()>=eachTeam.getLevel().intValue()){
                    //大于等于说明该用户理应给我打款一次 判断转账
                    if (transferDetails.size()<=0){
                        leakOrderCount+=1;
                    }
                }

                if (sysUserSensitive.getUserSensitiveList()!=null&&sysUserSensitive.getUserSensitiveList().size()>0){
                    getSubUserNumber(sysUserSensitive.getUserSensitiveList(),eachTeam.getLevel()+1,currentUserId,listResult);
                }
            }

            eachTeam.setLeakOrderNum(leakOrderCount);
            eachTeam.setReceivedOrderNum(ReceivedOrderCount);
            listResult.add(eachTeam);
        }
    }

    /**
     * 新增转账明细
     * @param sysUserSensitiveTemp
     * @param amount
     * @param supUserId
     */
    private void insertTransferDetail(SysUserSensitive sysUserSensitiveTemp, BigDecimal amount, long supUserId) {
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setFromId(sysUserSensitiveTemp.getUserId());//付款人id
        transferDetail.setToId(supUserId);//收款用户id
        transferDetail.setUpLevel(sysUserSensitiveTemp.getUserLevel()+1);//上升等级为+1
        transferDetail.setAmount(amount.doubleValue());
        transferDetail.setStatus(TaskStatusEnum.PENDING_PAYMENT.getValue());//待付款
        transferDetailService.insertTransferDetail(transferDetail);
    }

    /**
     * 获取最近十级用户id
     * @param invitationId
     * @return
     */
    private Long getTheLatestTenUserId(Long invitationId){
        SysUserSensitive invitationUser = this.selectSysUserSensitiveByUserId(invitationId);
        if (invitationUser!=null){
            //校验等级是否符合邀请 不符合在找上层
            if (invitationUser.getUserLevel()==10) {
                return invitationUser.getUserId();
            }else{
                return getTheLatestTenUserId(invitationUser.getInvitationId());
            }
        }else{
            //给管理员用户打款
            return 1l;
        }
    }

}
