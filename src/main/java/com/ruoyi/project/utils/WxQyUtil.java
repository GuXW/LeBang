package com.ruoyi.project.utils;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WxQyUtil {
    /**企业id*/
    @Value("${wx.qy.corpid}")
    private  String corpid;
    /**应用密钥*/
    @Value("${wx.qy.corpsecret}")
    private  String corpsecret;
    /**accessToken请求路径*/
    @Value("${wx.qy.url.access_token}")
    private  String ACCESS_TOKEN_URL;
    /**登录授权请求路径*/
    @Value("${wx.qy.url.login}")
    private  String LOGIN_URL;
    /**读取用户信息*/
    @Value("${wx.qy.url.read_user}")
    private  String READ_USER_URL;
    /**用户敏感信息*/
    @Value("${wx.qy.url.user_sensitivity_information}")
    private  String USER_SENSITIVITY_INFORMATION_URL;
    /**部门信息*/
    @Value("${wx.qy.url.department_information}")
    private  String DEPARTMENT_INFORMATION_URL;
    /**部门详细信息*/
    @Value("${wx.qy.url.department_information_detail}")
    private  String DEPARTMENT_INFORMATION_DETAIL_URL;
    /**企业根据用户code获取用户信息*/
    @Value("${wx.qy.url.user_by_code}")
    private  String USER_BY_CODE_URL;


    /**
     * 企业获取token
     * @return
     */
    public  Map<String, Object> getQYAccessToken() {
        //没有则请求获取token
        Map<String, String> param = new HashMap<String, String>();
        param.put("corpid", corpid);
        param.put("corpsecret", corpsecret);
        //发送get请求
        String result = HttpUtils.sendGet(ACCESS_TOKEN_URL, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);
    }

    /**
     * 企业获取部门列表
     * @return
     */
    public  Map<String, Object> getDepartment(String... value) {
        Map<String, String> param = new HashMap<String, String>();
        if (value.length>0){
            System.out.println(value[0]);
            param.put("access_token",value[0]);
        }
        if (value.length>1) {
            param.put("id", value[1]);
        }
        System.out.println(DEPARTMENT_INFORMATION_URL);
        //发送get请求
        String result = HttpUtils.sendGet(DEPARTMENT_INFORMATION_URL, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);
    }


    /**
     * 企业登录授权
     * @param accessToken
     * @param code
     * @return map{ "errcode": 0, "errmsg": "ok","CorpId":"CORPID","UserId":"USERID","DeviceId":"DEVICEID", "user_ticket": "USER_TICKET"，"expires_in":7200}
     * 非企业用户 map{"errcode": 0, "errmsg": "ok","OpenId":"OPENID","DeviceId":"DEVICEID"}
     */
    public  Map<String,Object> wxQYLogin(String accessToken,String code){
        Map<String, String> param = new HashMap<String, String>();
        param.put("access_token",accessToken);
        param.put("js_code",code);
        param.put("grant_type","authorization_code");
        String result = HttpUtils.sendGet(LOGIN_URL, HttpUtils.getParam(param));
        //获取登录
        return JsonUtils.jsonToObject(result, Map.class);
    }

    /**
     * 企业在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。
     * 根据用户id获取信息
     *  @return map{ "errcode": 0,"errmsg": "ok", "userid": "zhangsan", "name": "李四","department": [1, 2],"order": [1, 2],"position": "后台工程师","mobile": "15913215421",
     *     "gender": "1","email": "zhangsan@gzdev.com","is_leader_in_dept": [1, 0],"avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
     *     "telephone": "020-123456", "enable": 1,"alias": "jackzhang","address": "广州市海珠区新港中路",
     *     "extattr": { "attrs": [ { "type": 0, "name": "文本名称","text": {"value": "文本"}}, { "type": 1, "name": "网页名称", "web": { "url": "http://www.test.com", "title": "标题" }}]},
     *     "status": 1,"qr_code": "https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx", "external_position": "产品经理",
     *     "external_profile": {"external_corp_name": "企业简称",
     *     "external_attr": [{"type": 0,"name": "文本名称","text": {"value": "文本"} },{ "type": 1,"name": "网页名称","web": {"url": "http://www.test.com","title": "标题"}},{"type": 2,"name": "测试app","miniprogram": {"appid": "wx8bd80126147df384","pagepath": "/index","title": "my miniprogram"}}]}
     * }
     */
    public  Map<String,Object> getUserDetail(String accessToken,String userId){
        Map<String, String> param = new HashMap<String, String>();
        param.put("access_token",accessToken);
        param.put("userid",userId);
        String userDetailResult = HttpUtils.sendGet(READ_USER_URL, HttpUtils.getParam(param));
       return JsonUtils.jsonToObject(userDetailResult, Map.class);
    }

    /**
     * 企业用户敏感信息 （获取访问用户敏感信息）
     * @param accessToken
     * @param userTicket
     * @return map{"errcode": 0,"errmsg": "ok","corpid":"wwxxxxxxyyyyy","userid":"lisi","name":"李四","gender":"1","avatar":"http://shp.qpic.cn/bizmp/xxxxxxxxxxx/0",
     *    "qr_code":"https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=vcfc13b01dfs78e981c"
     * }
     */
    public   Map<String,Object> getUserSensitivityInformation(String accessToken,String userTicket){
        Map<String, String> param = new HashMap<String, String>();
        //user_ticket
        param.put("user_ticket",userTicket);
        String result = HttpUtils.sendPost(USER_SENSITIVITY_INFORMATION_URL + accessToken, HttpUtils.getParam(param));
        return  JsonUtils.jsonToObject(result, Map.class);
    }

    /**
     * 根据accesstoken 和部门id 获取部门成员信息
     * @param accessToken
     * @param departmentId
     * @param fetchChild 是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门
     * @return
     * @throws Exception
     */
    public Map<String, Object> getDepartmentUserByDepartmentId(String accessToken, String departmentId,Integer fetchChild) throws Exception {
        Map<String, String> param = new HashMap<String, String>();
        if (StringUtils.isEmpty(accessToken)){
            throw new Exception("accessToken获取失败");
        }
        if (StringUtils.isEmpty(departmentId)) {
            throw new Exception("部门id获取失败");
        }
        param.put("access_token",accessToken);
        param.put("department_id",departmentId);
        param.put("fetch_child",fetchChild==null?"0":fetchChild+"");
        System.out.println(DEPARTMENT_INFORMATION_URL);
        //发送get请求
        String result = HttpUtils.sendGet(DEPARTMENT_INFORMATION_DETAIL_URL, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);

    }

    public Map<String, Object> getQyUserByCode(String accessToken, String code) throws Exception {

        Map<String, String> param = new HashMap<String, String>();
        if (StringUtils.isEmpty(accessToken)){
            throw new Exception("accessToken获取失败");
        }
        if (StringUtils.isEmpty(code)) {
            throw new Exception("code获取失败");
        }
        param.put("access_token",accessToken);
        param.put("code",code);
        //发送get请求
        String result = HttpUtils.sendGet(USER_BY_CODE_URL, HttpUtils.getParam(param));
        return JsonUtils.jsonToObject(result, Map.class);
    }
}
