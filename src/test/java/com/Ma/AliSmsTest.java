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
        sendSms.sendSms("13633012771", code);




//        System.err.println(AliSmsUtils.sendSms("13633012771", "666666"));
//        System.err.println(AliSmsUtils.sendSms("15224664012", "543210"));
//        System.err.println(AliSmsUtils.sendSms("18790842260", "6666"));
    }
}
