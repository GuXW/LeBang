package com.ruoyi.project.sms.qcloud;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.ruoyi.project.sms.ISMSService;
import com.ruoyi.project.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service(value = "qcloud")
public class QCloudSmsServiceImpl implements ISMSService {

    // 短信应用 SDK AppID
    @Value("${sms.qcloud.appid}")
    private int APP_ID; // SDK AppID 以1400开头
    // 短信应用 SDK AppKey
    @Value("${sms.qcloud.appkey}")
    private String APP_KEY;
    @Value("${sms.qcloud.smsSign}")
    private String smsSign;
    @Value("${sms.qcloud.templateId}")
    private int templateId; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请

    @Autowired
    private RedisUtils redisUtils;

    public String sendCode(String mobile) throws Exception {
        String randomCode = getRandomCode(4);
//        SmsSingleSenderResult results = null;
//        Map<String, Object> resultmap = new HashMap<>();
//        SmsSingleSender ssender = new SmsSingleSender(APP_ID, APP_KEY);
//        results = ssender.sendWithParam("86", mobile, Integer.valueOf(templateId),new String[]{randomCode} , smsSign,"", "");
//        if (results!=null&&results.result != 0) {
//           throw  new Exception("短信发送失败，原因:" + results.errMsg);
//        }
        System.out.println(randomCode);
        redisUtils.set(mobile,randomCode);
        redisUtils.expire(mobile,60000);
        //return results.errMsg;
        return randomCode;
    }

    //获取随机验证码
    private String getRandomCode(int length) {
        StringBuffer code = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }


}
