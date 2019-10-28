package com.ruoyi.project.lb.api;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.content.UserConfirmEnum;
import com.ruoyi.project.lb.article.service.IArticleService;
import com.ruoyi.project.lb.detail.domain.TransferDetail;
import com.ruoyi.project.lb.detail.service.ITransferDetailService;
import com.ruoyi.project.lb.sensitive.domain.SysUserSensitive;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import com.ruoyi.project.sms.ISMSService;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import com.ruoyi.project.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.loadtime.Aj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("app用户接口")
@Controller
@RequestMapping("/api/user")
public class ApiUserController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Autowired
    private ITransferDetailService transferDetailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IArticleService articleService;


    @Autowired
    @Qualifier("qcloud")
    private ISMSService smsService;

    /**
     * 根据手机号请求手机验证码
     *
     * @return
     */
    @ApiOperation("获取手机验证码")
    @GetMapping("/mobile_verification_code")
    @ResponseBody
    public AjaxResult mobileVerificationCode(String mobile) {
        try {
            String resultMsg = smsService.sendCode(mobile);
            return AjaxResult.success(resultMsg);
        } catch (Exception e) {
            log.error("短信获取失败", e);
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 注册
     *
     * @param mobile
     * @param checkCode
     * @param password
     * @param inviteCode
     * @return
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    @ResponseBody
    public AjaxResult register(String mobile, String checkCode, String password, String inviteCode) {

        if (StringUtils.isBlank(mobile)) {
            return AjaxResult.error("手机号不能为空");
        }
        if (StringUtils.isBlank(checkCode)) {
            return AjaxResult.error("验证码不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return AjaxResult.error("密码不能为空");
        }
        if (StringUtils.isBlank(inviteCode)) {
            return AjaxResult.error("邀请码不能为空");
        }
        //验证短信验证码
        AjaxResult x = checkMobileCode(mobile, checkCode);
        if (x != null) return x;

        try {
            return userService.insertUserByApp(mobile, password, inviteCode);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("新增失败回滚");
        }
    }

    /**
     * 根据用户id获取用户信息
     *
     * @return
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/user_by_id")
    @ResponseBody
    public AjaxResult mobileVerificationCode(@RequestHeader("userId") Long userId) {
        SysUserSensitive sysUserSensitive = sysUserSensitiveService.selectSysUserSensitiveByUserId(userId);
        if (sysUserSensitive != null) {
            TransferDetail transferDetail=new TransferDetail();
            transferDetail.setToId(sysUserSensitive.getUserId());
            transferDetail.setStatus(2);//待确认
            List<TransferDetail> transferDetails = transferDetailService.selectTransferDetailList(transferDetail);
            sysUserSensitive.setPayNoConfirmNum(transferDetails!=null?transferDetails.size():0);
        }
        return AjaxResult.success(sysUserSensitive);
    }

    @ApiOperation("重置密码")
    @PostMapping("/reset_pwd")
    @ResponseBody
    public AjaxResult resetPwd(String mobile, String checkCode, String password) {
        if (StringUtils.isBlank(mobile)) {
            return AjaxResult.error("登录账号不能为空");
        }
        if (StringUtils.isBlank(checkCode)) {
            return AjaxResult.error("验证码不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return AjaxResult.error("密码不能为空");
        }

        //验证短信验证码
        AjaxResult x = checkMobileCode(mobile, checkCode);
        if (x != null) return x;

        User user = new User();
        user.setPassword(password);
        user.setLoginName(mobile);
        User userTemp = userService.selectUserByPhoneNumber(mobile);
        if (userTemp == null) {
            return AjaxResult.error("获取不到用户");
        }
        user.setUserId(userTemp.getUserId());
        return AjaxResult.success(userService.resetUserPwd(user));
    }

    /**
     * 获取联系我们文章
     *
     * @return
     */
    @ApiOperation("根据文章id获取文章")
    @GetMapping("/contact_us")
    @ResponseBody
    public AjaxResult contactUs(Long id) {
        return AjaxResult.success(articleService.selectArticleById(id));
    }




    /**
     * 校验手机验证码是否正确
     *
     * @param mobile
     * @param checkCode
     * @return
     */
    private AjaxResult checkMobileCode(@RequestParam String mobile, @RequestParam String checkCode) {
        //校验手机验证码
        //获取缓存的随机码
        String code = redisUtils.get(mobile);
        //验证随机码是否为空
        if (StringUtils.isBlank(code)) {
            return AjaxResult.error("验证码已过期");
        }
        //验证短信验证码是否相同
        if (!checkCode.equals(code)) {
            return AjaxResult.error("短信验证码错误");
        }
        //删除验证码缓存
        redisUtils.del(mobile);
        return null;
    }

    @ApiOperation("编辑身份证")
    @PostMapping("/edit_identity")
    @ResponseBody
    public AjaxResult editIdentity(Long id, String idCardName, String idCardNum, String idCardPositive, String idCardNegative) {
        if (id == null) {
            return AjaxResult.error("获取不到用户id");
        }

        if (StringUtils.isBlank(idCardName)) {
            return AjaxResult.error("身份证名称不能为空");
        }

        if (StringUtils.isBlank(idCardNum)) {
            return AjaxResult.error("身份证号不能为空");
        }

        if (StringUtils.isBlank(idCardPositive)) {
            return AjaxResult.error("身份证正面图片不能为空");
        }

        if (StringUtils.isBlank(idCardNegative)) {
            return AjaxResult.error("身份证反面图片不能为空");
        }
        //设置身份证信息 并更新
        SysUserSensitive sysUserSensitive = new SysUserSensitive();
        sysUserSensitive.setId(id);
        sysUserSensitive.setUpdateTime(DateUtils.getNowDate());
        sysUserSensitive.setIdCardName(idCardName);
        sysUserSensitive.setIdCardNum(idCardNum);
        sysUserSensitive.setIdCardNegative(idCardNegative);
        sysUserSensitive.setIdCardPositive(idCardPositive);
        sysUserSensitive.setStatus(UserConfirmEnum.no_check.getValue() + "");
        return AjaxResult.success(sysUserSensitiveService.updateSysUserSensitive(sysUserSensitive));
    }

    @ApiOperation("编辑收款信息")
    @PostMapping("/edit_make_collections")
    @ResponseBody
    public AjaxResult editMakeCollections(@RequestHeader("userId") Long userId, Long id, Integer receivingChannel, String receivingAccount, String receivingChart, String checkCode) {
        if (id == null) {
            return AjaxResult.error("获取不到用户id");
        }

        if (StringUtils.isBlank(receivingAccount)) {
            return AjaxResult.error("收款账号不能为空");
        }

        if (StringUtils.isBlank(receivingChart)) {
            return AjaxResult.error("收款码不能为空");
        }

        if (StringUtils.isBlank(checkCode)) {
            return AjaxResult.error("手机验证码不能为空");
        }
        String loginName = redisUtils.get(RedisUtils.LOGIN_MOBILE + userId);
        if (StringUtils.isEmpty(loginName)) {
            return AjaxResult.error("获取不到用户对应手机号");
        }
        //验证短信验证码
        AjaxResult x = checkMobileCode(loginName, checkCode);
        if (x != null) return x;

        SysUserSensitive sysUserSensitiveTemp = sysUserSensitiveService.selectSysUserSensitiveByUserId(userId);
        if (sysUserSensitiveTemp == null) {
            return AjaxResult.error("获取不到用户信息");
        }

        if (sysUserSensitiveTemp.getReceivingAccount() != null) {
            return AjaxResult.error("已上传收款信息，暂不可修改！");
        }

        //设置收款信息 并更新
        SysUserSensitive sysUserSensitive = new SysUserSensitive();
        sysUserSensitive.setId(id);
        sysUserSensitive.setUpdateTime(DateUtils.getNowDate());
        sysUserSensitive.setReceivingAccount(receivingAccount);
        sysUserSensitive.setReceivingChart(receivingChart);
        sysUserSensitive.setReceivingChannel(receivingChannel);
        return AjaxResult.success(sysUserSensitiveService.updateSysUserSensitive(sysUserSensitive));
    }

    /**
     * 我的下级用户（好友）
     */
    @ApiOperation("我的好友")
    @GetMapping("/sensitive_sub")
    @ResponseBody
    public AjaxResult user(@RequestHeader("userId") Long userId) {
        SysUserSensitive sysUserSensitive = new SysUserSensitive();
        sysUserSensitive.setInvitationId(userId);
        List<SysUserSensitive> sysUserSensitives = sysUserSensitiveService.selectSysUserSensitiveList(sysUserSensitive);
        List<Map<String, Object>> resultListMap = new ArrayList<>();
        for (SysUserSensitive sysUserSensitiveTemp :
                sysUserSensitives) {
            Map<String, Object> result = new HashMap<>();
            result.put("userName", sysUserSensitiveTemp.getUserName());
            result.put("mobile", sysUserSensitiveTemp.getPhonenumber());
            resultListMap.add(result);
        }
        return AjaxResult.success(resultListMap);
    }

    @ApiOperation("我的团队")
    @GetMapping("/my_team")
    @ResponseBody
    public AjaxResult myTeam(@RequestHeader("userId") Long userId) {
        try {
            return AjaxResult.success(sysUserSensitiveService.queryMyTeam(userId));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("最近激活人")
    @GetMapping("/get_new_activationer")
    @ResponseBody
    public AjaxResult getNewActivationer(){
        try {
            return AjaxResult.success(sysUserSensitiveService.getNewActivationer());
        } catch (Exception e) {
            return  AjaxResult.error(e.getMessage());
        }
    }


}
