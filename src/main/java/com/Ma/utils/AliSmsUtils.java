package com.Ma.utils;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Data
@ConfigurationProperties("sms")
public class AliSmsUtils {

    private  Client client;

    /**
     * 获取你的accessKeyId
     */
    private  String accessKeyId;
    /**
     * 获取你的accessKeySecret
     */

    private  String accessKeySecret;
    /**
     * 获取你的短信模板CODE
     */

    private  String TemplateCode;
    /**
     * 获取你的短信签名名称
     */
    private  String SignName;




//   static  {
//        Config config = new Config()
//                // 必填：AccessKey ID
//                .setAccessKeyId(accessKeyId)
//                // 必填：AccessKey Secret
//                .setAccessKeySecret(accessKeySecret);
//        // 设置请求地址
//        config.endpoint = "dysmsapi.aliyuncs.com";
//        try {
//            client = new Client(config);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostConstruct
    public void init() {
        Config config = new Config()
                .setAccessKeyId(Optional.ofNullable(accessKeyId).orElseThrow(() ->
                        new IllegalArgumentException("accessKeyId must not be null")))
                .setAccessKeySecret(Optional.ofNullable(accessKeySecret).orElseThrow(() ->
                        new IllegalArgumentException("accessKeySecret must not be null")));

        // 设置请求地址
        config.endpoint = "dysmsapi.aliyuncs.com";

        try {
            this.client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SMS client", e);
        }
    }


    /**
     *
     * @param phone 手机号
     * @param code 验证码
     * @return
     */

    public void sendSms(String phone, String code) throws Exception {

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(SignName)
                .setTemplateCode(TemplateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":"+code+"}");
        RuntimeOptions runtime = new RuntimeOptions();

            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);


    }

    /**
     * @return 返回4位随机生成的验证码
     */
    public static String Random4() {
        int code = RandomUtil.randomInt(1000, 9999);
        return String.valueOf(code);
    }

    /**
     * @return 返回6位随机生成的验证码
     */
    public static String Random6() {
        int code = RandomUtil.randomInt(100000, 999999);
        return String.valueOf(code);
    }
}
