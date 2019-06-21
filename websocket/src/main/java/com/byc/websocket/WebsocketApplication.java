package com.byc.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
public class WebsocketApplication {

	@Autowired
	private SimpMessagingTemplate template;//stomp消息模板

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

	@MessageMapping("/subscribe")
	public void subscribe(ReceiveMessage rm) {
		for(int i =1;i<=20;i++) {
			//广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
			template.convertAndSend("/topic/getResponse", rm.getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@MessageMapping("/subscribe")
	@SendTo("/topic/liu")//可以实现消息转发类似SimpMessagingTemplate
	public String trans(ReceiveMessage rm) {
		return "通知";
	}

	@SubscribeMapping("/topic")
	@SendTo("/topic/liu")//订阅时会请求到这里
	public String subscribe() {
		return "通知";
	}

	@MessageMapping("/queue")
	public void queuw(ReceiveMessage rm) {
		System.out.println("进入方法");
		for(int i =1;i<=20;i++) {
            /*广播使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
            "/user/" + 用户Id + "/message",其中"/user"是固定的*/
			template.convertAndSendToUser("zhangsan","/message",rm.getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
