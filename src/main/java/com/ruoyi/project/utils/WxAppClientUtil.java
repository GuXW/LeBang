package com.ruoyi.project.utils;

import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.project.content.TemplateTypeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WxAppClientUtil {

    @Value("${wx.app_client.appid}")
    private  String appid;
    @Value("${wx.app_client.secretid}")
    private  String secret;

    @Value("${wx.app_client.url.getAccessToken}")
    private  String APP_TOKEN_URL;
    @Value("${wx.app_client.url.code2Session}")
    private  String CODE_2_SESSION_URL;
    @Value("${wx.app_client.url.sendMsg}")
    private  String SEND_MSG_URL;



    /**
     * 小程序获取token
     * @return
     */
    public  Map<String, Object> getWXAPPAccessToken() {
        System.out.println("appid:" + appid+"  secret"+secret +"    app_token_url:"+APP_TOKEN_URL);
        //没有则请求获取token
        Map<String, String> param = new HashMap<String, String>();
        param.put("grant_type","client_credential");
        param.put("appid", appid);
        param.put("secret", secret);
        //发送get请求
        String result = HttpUtils.sendGet(APP_TOKEN_URL, HttpUtils.getParam(param));
        System.out.println("result:" +result);
        return JsonUtils.jsonToObject(result, Map.class);
    }

    /**
     * 小程序获取token
     * @return
     */
    public  Map<String, Object> getWXAPPCode2Session(String code) {
        //没有则请求获取token
        Map<String, String> param = new HashMap<String, String>();

        param.put("appid", appid);
        param.put("secret", secret);
        param.put("js_code", code);
        param.put("grant_type","authorization_code");
        //发送get请求
        String result = HttpUtils.sendGet(CODE_2_SESSION_URL, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);
    }


    public Map<String,Object> sendMsg(String accessToken, String openId, TemplateTypeEnum type, String formId, Map<String,Object> data){
        Map<String, String> param = new HashMap<String, String>();
        param.put("touser",openId);
        param.put("template_id",type.getValue().toString());
        param.put("form_id",formId);
        param.put("data",JsonUtils.objectToJson(data));
        String result = HttpUtils.sendPost(SEND_MSG_URL+accessToken, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);
    }



}
