package com.raythonsoft;

import com.raythonsoft.sso.SsoApplication;
import com.raythonsoft.sso.util.MD5Util;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@Log4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@WebAppConfiguration
public class ServerApplicationTests {

	@Test
	public void contextLoads() {
		log.info(MD5Util.md5("authauth"));
	}

}
