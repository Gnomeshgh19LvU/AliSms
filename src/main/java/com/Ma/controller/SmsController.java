package com.Ma.controller;

import com.Ma.utils.AliSmsUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/send")
public class SmsController {
    private final AliSmsUtils aliSmsUtils;

    public SmsController(AliSmsUtils aliSmsUtils) {
        this.aliSmsUtils = aliSmsUtils;
    }

    @PostMapping(value = "/send_sms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> sendVerificationCode(@RequestBody @Valid SmsRequest smsRequest) {
        String phone = smsRequest.getPhoneNumber();

        // 验证手机号码
        if (!isValidPhoneNumber(phone)) {
            throw new IllegalArgumentException("手机号格式不正确");
        }

        try {
            String code = AliSmsUtils.Random6(); // 根据需求生成验证码
            aliSmsUtils.sendSms(phone, code);

            // 返回包含手机号和真实验证码的JSON
            Map<String, String> response = new HashMap<>();
            response.put("phone", phone);
            response.put("code", code);
            response.put("message", "验证码已发送");
            return response;
        } catch (Exception e) {
            throw new RuntimeException("发送验证码失败", e);
        }
    }

    // 校验手机号格式
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^1[3-9]\\d{9}$"; // 中国内地手机号格式
        return Pattern.matches(regex, phoneNumber);
    }

    // 创建一个新的类来表示请求体
    public static class SmsRequest {
        private String phoneNumber;

        // getter 和 setter 方法
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
