## 一.使用阿里云短信服务调用接口完成短信验证码功能
### 1.获取秘钥

accessKeyId，

accessKeySecret，

SignName，(短信模板)

TemplateCode，(短信签名名称)

#### 1.1获取accessKeyId，accessKeySecret

accessKeyId，accessKeySecret可以直接获取  


  ![ali-1](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-1.png)  


点击AccessKey然后无脑确认即可获取AccessKey  


![ali-2](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-2.png)   

**Ps:创建一个即可，暂时不需要这么多，并且请保存好自己的AccessKey**

#### 1.2获取SignName，TemplateCode

访问ali的工作台，搜索短信服务或者直接输入sms

**点击国内消息完成1.资质管理，2.签名管理，3.模板管理**

![ali-3](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-3.png)

![ali-4](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-4.png)

![ali-5](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-5.png)

Ps:在添加签名的时候建议不要和我一样用线上测试，可以用测试或学习，可以获得1000条免费短信的数量，好像是三个月吧。应用场景和场景说明最好填写一些努力学习的字眼，但是千万不要提到**测试**

### 2.完成测试并获取源码
#### 2.1完成API测试
回到快速学习与测试**通过API发送短信**绑定测试手机号并写入相应的签名和模板完成测试

#### 2.2获取源码
点击概览，找到Open AI开发者门户，在上方找到SDK选择语言为java，右侧有SDK信息，可以获取相应的Maven依赖，将源码打包并部署到自己的电脑中即可

### 3.创建工程，完成测试

因为我用的代码和ali上的源码有出入，我就以我的代码来讲了

#### 3.1 测试
（1）我们首先先创建一个简单的Springboot项目并导入相关依赖

```xml
<dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>com.aliyun</groupId>
      <artifactId>dysmsapi20170525</artifactId>
      <version>2.0.9</version>
    </dependency>
    <dependency>
      <groupId>cn.hutool</groupId>
      <artifactId>hutool-all</artifactId>
      <version>5.8.15</version>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.28</version>
      <scope>compile</scope>
    </dependency>

    <dependency>
      <groupId>jakarta.validation</groupId>
      <artifactId>jakarta.validation-api</artifactId>
      <version>2.0.2</version>
    </dependency>
  </dependencies>
```

（2）创建一个Utils包并命名为AliSmsUtils，并写入代码（源码）



```java
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

```

（3）我们还必须在application.yml中配置相关信息



```yml
sms:
  accessKeyId: 
  accessKeySecret: 
  SignName: 
  TemplateCode: 
```

以上的信息是在阿里云官方弄好的产品密钥，用自己的 **注意 在:之后要加一个空格才写入东西，后面的秘钥可以不用加双引号**

（4）创建测试类

在test包下创建AliSmsTest类

```java
package com.Ma;
import com.Ma.utils.AliSmsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AliSmsTest {
    
    @Autowired
    private AliSmsUtils sendSms;

    @Test
     void send() throws Exception {

        String code = sendSms.Random6();
        System.out.println(code);
        sendSms.sendSms("自己的手机号", code);
    }
}
```

**冷知识，因为我们创建的是线上测试，测试与学习，手机号码只能是在2.1中绑定的自己的手机号，最多能绑定5个手机号，虽然有点少但是用于测试或学习已经够了**

然后运行就可以收到自己的手机短信验证码啦

![ali-6](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-6.png)

![ali-7](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-7.png)

#### 3.2 完成接口调用

（1）创建controller包，创建SMSController类



```java
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

```

#### 3.3 在postman中查收

由于我们是接受短信验证码，所以我们使用post请求

然后我们访问的域名端口是**http://localhost:8080/send/send_sms**

可自由更改api



在params中我们输入

key:phone

value:自己的手机号



在Headers请求头中我们新增

key:Content-Type

value:application/json



在body中更改为raw，json

并输入json格式为



```json
{
    "phoneNumber":"自己的手机号"
}
```
#### 3.4 验收

回到我们的后端代码中，启动服务，**温馨小提示，Springboot至少需要jdk17不然会报错**

到postman中点击send，同样可以收到短信啦

![ali-8](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-8.png)

![ali-9](https://kirk.cdkm.com/convert/file/st3k1aqnjz0p79s2ff8ouce39s0j29j9/ali-9.png)







以上就是用java调用ali的短信服务完成接受短信验证码的功能啦

感谢观看！~



















