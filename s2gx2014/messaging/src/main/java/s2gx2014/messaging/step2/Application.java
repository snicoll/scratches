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

package s2gx2014.messaging.step2;

import java.util.Date;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s2gx2014.messaging.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * This step changes the signature to accept a mandatory new header. We switch from
 * {@link JmsTemplate} to {@link JmsMessagingTemplate} to use the messaging abstraction
 * to set that custom header.
 *
 * @author Stephane Nicoll
 */
@ComponentScan
@EnableAutoConfiguration
@EnableJms
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private JmsMessagingTemplate messagingTemplate;

	@Override
	public void run(String... args) throws Exception {
		Order order = new Order("123", new Date(), "customer-foo-123");
		Message<Order> message = MessageBuilder.withPayload(order)
				.setHeader("orderType", "sell").build();
		logger.info("Sending order...");
		messagingTemplate.send("order", message);
	}

	@Component
	public static class OrderMessageHandler {

		@JmsListener(destination = "order")
		public void process(Order order, @Header String orderType) {
			logger.info("----------------------");
			logger.info("Received " + order + " with type " + orderType);
			logger.info("----------------------");
		}
	}

	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
		return new JmsMessagingTemplate(jmsTemplate);
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
