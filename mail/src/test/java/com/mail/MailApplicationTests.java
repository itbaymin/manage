package com.mail;

import com.mail.service.MailMessage;
import com.mail.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

	@Autowired
	MailService mailService;

	@Test
	public void contextLoads() {
		MailMessage message = new MailMessage();
		message.setMessageCode("MissingParameter");
		message.setMessageStatus("Failed");
		message.setCause("缺少参数,请确认");
		try {
			mailService.sendMessageMail(message, "测试消息通知", "mail/message.ftl", "996948901@qq.com", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReceive(){
		try {
			mailService.receiveMail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
