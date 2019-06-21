package com.byc.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketserverApplication.class, args);
	}

	/*@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
		return factory -> {
			JettyServletWebServerFactory jettyServletWebServerFactory = (JettyServletWebServerFactory) factory;
			QueuedThreadPool queuedThreadPool = new QueuedThreadPool(200,5,30000);
			jettyServletWebServerFactory.setThreadPool(queuedThreadPool);
		};
	}*/

}
