/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package s2gx2014.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Stephane Nicoll
 */
@EnableAutoConfiguration
@ComponentScan
@EnableJms
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Component
	static class OrderClient implements CommandLineRunner {

		private static final Logger logger = LoggerFactory.getLogger(OrderClient.class);

		private final JmsMessagingTemplate messagingTemplate;

		@Autowired
		OrderClient(JmsMessagingTemplate messagingTemplate) {
			this.messagingTemplate = messagingTemplate;
		}

		@Override
		public void run(String... args) throws Exception {
			Message<Order> orderMessage = MessageBuilder.withPayload(new Order("123"))
					.setHeader("orderType", "sell").build();
			Message<?> message = messagingTemplate.sendAndReceive("orderRequest", orderMessage);
			logger.info("---------------------------------------");
			logger.info("Received reply " + message);
			logger.info("---------------------------------------");
		}
	}
}
