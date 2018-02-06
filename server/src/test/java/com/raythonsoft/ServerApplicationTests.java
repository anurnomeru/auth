package com.raythonsoft;

import com.raythonsoft.sso.SsoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
@WebAppConfiguration
public class ServerApplicationTests {

	@Test
	public void contextLoads() {

	}

}
