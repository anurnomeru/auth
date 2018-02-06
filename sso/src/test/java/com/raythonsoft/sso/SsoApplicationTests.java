package com.raythonsoft.sso;

import com.raythonsoft.sso.util.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SsoApplication.class)
@WebAppConfiguration
public class SsoApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(MD5Util.md5("authauth"));
    }

}
