package com.ruoyi.project.lb.api;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.chart.domain.RotationChart;
import com.ruoyi.project.lb.chart.service.IRotationChartService;
import com.ruoyi.project.lb.version.domain.AppVersion;
import com.ruoyi.project.lb.version.service.IAppVersionService;
import com.ruoyi.project.utils.JsonUtils;
import com.ruoyi.project.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Api("版本号管理接口")
@Controller
@RequestMapping("/api/app_version")
public class ApiAppVersionController {

    @Autowired
    private IAppVersionService appVersionService;
    @Autowired
    private RedisUtils redisUtils;


    @ApiOperation("获取最新版本号")
    @GetMapping("/get_new_version")
    @ResponseBody
    public AjaxResult getNewVersion(Integer type)
    {
        String appVersionStr = redisUtils.get("app_version_" + type);
        if (StringUtils.isNotBlank(appVersionStr)){
            return AjaxResult.success(appVersionStr);
        }else{
            AppVersion appVersion=new AppVersion();
            appVersion.setAppType(type);
            AppVersion appVersionTemp = appVersionService.selectNewAppVersionByType(appVersion);
            if (appVersionTemp!=null){
                redisUtils.set("app_version_" +appVersionTemp.getAppType(), JsonUtils.objectToJson(appVersionTemp));
                appVersionStr = redisUtils.get("app_version_" + type);
            }
            return AjaxResult.success(appVersionStr);
        }
    }



}
