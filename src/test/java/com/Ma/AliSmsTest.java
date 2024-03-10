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
        sendSms.sendSms("自己的号码", code);

    }
}
