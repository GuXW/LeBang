package com.ruoyi.project.lb.api;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.content.TaskStatusEnum;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import com.ruoyi.project.lb.detail.service.ITransferDetailService;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.project.lb.task.domain.ActivityTask;
import com.ruoyi.project.lb.task.domain.ActivityTaskDetail;
import com.ruoyi.project.lb.task.service.IActivityTaskDetailService;
import com.ruoyi.project.lb.task.service.IActivityTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.framework.web.domain.AjaxResult.error;

@Api("app任务接口")
@Controller
@RequestMapping("/api/task")
public class ApiTaskController {

    private static final Logger log = LoggerFactory.getLogger(ApiTaskController.class);

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Autowired
    private ITransferDetailService transferDetailService;

    @Autowired
    private IActivityTaskDetailService activityTaskDetailService;

    @Autowired
    private IActivityTaskService activityTaskService;

    @ApiOperation("获取转账明细列表")
    @GetMapping("/transfer_detail_list_by_userId")
    @ResponseBody
    public AjaxResult transferDetailListByUserId(@RequestHeader("userId") Long userId,Integer taskType){
        //查看是请求任务获取付款信息还是获取打款信息
        TransferDetail transferDetail =new TransferDetail();
        if (taskType==null){
            AjaxResult.error("获取不到转账类型");
        }

        if (taskType==1){
            //付款人是userId
            transferDetail.setFromId(userId);
            transferDetail.setNoStatus(3);
        }else if (taskType==2){
            //收款人是userId
            transferDetail.setToId(userId);
            transferDetail.setStatus(TaskStatusEnum.NO_CONFIRM.getValue());
        }else{
            AjaxResult.error("获取不到对应类型");
        }
        List<TransferDetail> transferDetails = transferDetailService.selectTransferDetailList(transferDetail);
        if (transferDetail!=null&&transferDetails.size()>0){
            for (TransferDetail transferDetailTemp:
                    transferDetails) {
                if (StringUtils.isNotBlank(transferDetailTemp.getFromIdCardNum())){
                    transferDetailTemp.setFromIdCardNum(encryptedIDCard(transferDetailTemp.getFromIdCardNum()));
                }
                if (StringUtils.isNotBlank(transferDetailTemp.getToIdCardNum())){
                    transferDetailTemp.setToIdCardNum(encryptedIDCard(transferDetailTemp.getToIdCardNum()));
                }
            }
        }

        return AjaxResult.success(transferDetails);
    }

    private String encryptedIDCard(String idCardNum) {
        if (StringUtils.isNotBlank(idCardNum)){
            StringBuffer sb=new StringBuffer(idCardNum);
            sb.replace(6,14,"****");
            return sb.toString();
        }

        return null;

    }

    //创建任务
    @ApiOperation("创建任务")
    @PostMapping("/create_task")
    @ResponseBody
    public AjaxResult createTask(@RequestBody SysUserSensitive sysUserSensitive){
        try {
            return sysUserSensitiveService.createTask(sysUserSensitive);
        } catch (Exception e) {
            log.error("创建任务失败",e);
            return error(e.getMessage());
        }
    }

    //升级请求
    @ApiOperation("升级请求")
    @PostMapping("/upgrade")
    @ResponseBody
    public AjaxResult upgrade(@RequestHeader("userId") Long userId){
        try {
            return sysUserSensitiveService.upgrade(userId);
        } catch (Exception e) {
            log.error("升级请求失败",e);
            return error(e.getMessage());
        }
    }

    //提交打款凭证
    @ApiOperation("提交打款凭证")
    @PostMapping("/submit_payment_voucher")
    @ResponseBody
    public AjaxResult submitPaymentVoucher(Long id,String paymentCertificate){

        if (id == null){
            return AjaxResult.error("订单id不能为空");
        }


        if (StringUtils.isBlank(paymentCertificate)){
            return AjaxResult.error("付款凭证不能为空");
        }

        TransferDetail  transferDetail =new TransferDetail();
        transferDetail.setId(id);
        transferDetail.setPaymentCertificate(paymentCertificate);
        transferDetail.setStatus(2);

        try {
            return AjaxResult.success(transferDetailService.updateTransferDetail(transferDetail));
        } catch (Exception e) {
            e.printStackTrace();
            return error("提交凭证失败",e);
        }
    }


    //确认收款
    @ApiOperation("确认收款")
    @PostMapping("/confirm_receipt")
    @ResponseBody
    public AjaxResult  confirmReceipt(@RequestHeader("userId") Long userId,Long id){

        try {
            return transferDetailService.confirmUpLevel(userId,id,3);
        } catch (Exception e) {
            log.error("确认收款失败",e);
            return error(e.getMessage());
        }
    }

    //根据用户id获取计划详情
    @ApiOperation("根据用户id获取计划详情")
    @GetMapping("/task_detail")
    @ResponseBody
    public AjaxResult  taskDetail(@RequestHeader("userId") Long userId){
        return transferDetailService.taskDetail(userId);
    }

    //根据转账明细id获取转账明细详情
    @ApiOperation("根据转账明细id获取转账明细详情")
    @GetMapping("/task_detail_by_id")
    @ResponseBody
    public AjaxResult  taskDetailById(Long id){
        TransferDetail transferDetail = transferDetailService.selectTransferDetailById(id);
        if (StringUtils.isNotBlank(transferDetail.getFromIdCardNum())){
            transferDetail.setFromIdCardNum(encryptedIDCard(transferDetail.getFromIdCardNum()));
        }
        if (StringUtils.isNotBlank(transferDetail.getToIdCardNum())){
            transferDetail.setToIdCardNum(encryptedIDCard(transferDetail.getToIdCardNum()));
        }
        return AjaxResult.success(transferDetail);
    }


    @ApiOperation("获取当前活动")
    @GetMapping("/current_activity_task")
    @ResponseBody
    public AjaxResult currentActivityTask(@RequestHeader("userId") Long userId){
        return activityTaskService.queryCurrentActivityTask(userId);
    }


    //活动记录
    @ApiOperation("获取活动记录")
    @GetMapping("/activity_task")
    @ResponseBody
    public AjaxResult activityTask(@RequestHeader("userId") Long userId){
        ActivityTaskDetail activityTaskDetail=new ActivityTaskDetail();
        activityTaskDetail.setUserId(userId);
        return AjaxResult.success(activityTaskDetailService.selectActivityTaskDetailList(activityTaskDetail));
    }

    //根据活动明细id获取活动明细
    @ApiOperation("根据活动明细id获取活动明细")
    @GetMapping("/activity_by_id")
    @ResponseBody
    public AjaxResult activityById(Long id){
        if (id == null){
            return AjaxResult.error("活动明细id不能为空");
        }
        return AjaxResult.success(activityTaskDetailService.selectActivityTaskDetailById(id));
    }

    //参与活动 活动id
    @ApiOperation("参与活动")
    @PostMapping("/part_activities")
    @ResponseBody
    public AjaxResult partActivities(@RequestHeader("userId")Long userId,Long id){
        //判断活动是否存在
        if (id == null){
            return AjaxResult.error("活动id不能为空");
        }
        ActivityTask activityTask = activityTaskService.selectActivityTaskById(id);
        if (activityTask == null ){
            return AjaxResult.error("活动不存在");
        }

        if (activityTask.getActivityState()==2){
            return AjaxResult.error("活动已结束");
        }


        ActivityTaskDetail activityTaskDetail=new ActivityTaskDetail();
        activityTaskDetail.setTaskId(id);
        activityTaskDetail.setUserId(userId);
        activityTaskDetail.setCid(userId);
        activityTaskDetail.setStatus(1);
        activityTaskDetail.setCreateTime(DateUtils.getNowDate());
        activityTaskDetail.setWinnerStatus(0);

        try {
            return AjaxResult.success(activityTaskDetailService.insertActivityTaskDetail(activityTaskDetail));
        } catch (Exception e) {
            log.error("参与活动异常",e);
            return error(e.getMessage());
        }
    }

}
