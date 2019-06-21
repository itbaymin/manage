package com.byc;

import com.byc.mq.sender.FanoutSender;
import com.byc.mq.sender.HelloSender;
import com.byc.mq.sender.TopicSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqApplicationTests {

	@Autowired
	private HelloSender helloSender;

	@Autowired
	private TopicSender topicSender;

	@Autowired
	private FanoutSender fanoutSender;

	@Test
	public void hello() throws Exception {

		for (int i=0;i<100;i++){
			helloSender.sendObj(i);
		}

	}

	@Test
	public void topic() throws Exception {
		topicSender.sendTopic();
		topicSender.sendTopic2();
	}

	@Test
	public void fanout() throws Exception {
		fanoutSender.send();
	}
}
