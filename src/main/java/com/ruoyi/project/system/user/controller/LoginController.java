package com.ruoyi.project.system.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.project.content.LoginTypeEnum;
import com.ruoyi.project.utils.RedisUtils;
import com.ruoyi.project.utils.TokenProccessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Api("登录接口")
@Controller
public class LoginController extends BaseController
{
    @Autowired
    private RedisUtils redisUtils;


    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        redisUtils.set(RedisUtils.LOGIN_TYPE+username, LoginTypeEnum.PC.getValue());
        try
        {
            subject.login(token);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @PostMapping("/login_4_web_app")
    @ResponseBody
    public AjaxResult ajaxLogin4WebApp(String username, String password,String key,String validateCode)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
        Subject subject = SecurityUtils.getSubject();
        redisUtils.set(RedisUtils.LOGIN_TYPE+username, LoginTypeEnum.WEB_APP.getValue());

        try
        {
            if (StringUtils.isBlank(redisUtils.get(RedisUtils.LOGIN_CHECK_CODE + key.trim()))|| !redisUtils.get(RedisUtils.LOGIN_CHECK_CODE + key).equals(validateCode)){
                throw new Exception("验证码错误");
            }
            subject.login(token);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    @ApiOperation("app登录")
    @PostMapping("/login_4_app")
    @ResponseBody
    public AjaxResult ajaxLogin4App(String username, String password)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
        Subject subject = SecurityUtils.getSubject();
        redisUtils.set(RedisUtils.LOGIN_TYPE+username, LoginTypeEnum.APP.getValue());

        try
        {
            subject.login(token);
            String appToken = TokenProccessor.getInstance().makeToken();
            redisUtils.set(appToken,ShiroUtils.getUserId());
            redisUtils.set(RedisUtils.LOGIN_MOBILE+ShiroUtils.getUserId(),ShiroUtils.getLoginName());
            redisUtils.expire(appToken,3600000);
            redisUtils.expire(RedisUtils.LOGIN_MOBILE+ShiroUtils.getUserId(),3600000);
            Map<String,Object> result=new HashMap<String,Object>();
            result.put("token",appToken);
            result.put("userId",ShiroUtils.getUserId());
            return AjaxResult.success(result);
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }
}
