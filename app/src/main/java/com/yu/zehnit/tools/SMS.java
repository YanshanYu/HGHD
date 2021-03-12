package com.yu.zehnit.tools;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Random;

public class SMS {

    private static final String product = "Dysmsapi";
    private static final String domain = "dysmsapi.aliyuncs.com";

    private static final String accessKeyId = "1111111111111";
    private static final String accessKeySecret = "2222222222222222";

    private static String msgCode_check;

    /*public static void main(String[] args)throws Exception {
        SMSTest("13008726533");
    }*/

    public static SendSmsResponse SMSTest(String phone) throws Exception {
        //设置超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        String msgCode = getMsgCode();
        msgCode_check  = msgCode;
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);

        request.setPhoneNumbers(phone);
        //request.setPhoneNumbers("13008726533");
        request.setSignName("VertiQuest");
        request.setTemplateCode("SMS_175480501");
        request.setTemplateParam("{\"code\":\"" + msgCode + "\"}");
        request.setOutId("123321");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) { }
        return sendSmsResponse;
    }

    /**
     * 生成随机的6位数，短信验证码
     * @return
     */
    private static String getMsgCode() {
        int n = 6;
        StringBuilder code = new StringBuilder();
        Random ran = new Random();
        for (int i = 0; i < n; i++) {
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        return code.toString();
    }

    public static boolean checkCode(String code)throws Exception{
        if(code.equals(msgCode_check))
            return true;
        else
            return false;
    }
}