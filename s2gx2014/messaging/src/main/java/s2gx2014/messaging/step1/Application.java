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

package s2gx2014.messaging.step1;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s2gx2014.messaging.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * The first step is to demonstrate a basic listener. We create a very simple listener
 * and a client that sends a dummy order on the 'order' queue.
 *
 * <p>Note that this should fail unless the order queue has been created. You can add
 * {@code spring.hornetq.embedded.queues=order} to your {@code application.properties}
 * if you are using the embedded mode of HornetQ
 *
 * @author Stephane Nicoll
 */
@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void run(String... args) throws Exception {
		logger.info("Sending order...");
		jmsTemplate.send("order",
				session -> {
					Order order = new Order("123", new Date(), "customer-foo-123");
					return session.createObjectMessage(order);
				});
	}

	@Component
	public static class OrderMessageHandler {

		@JmsListener(destination = "order")
		public void process(Order order) {
			logger.info("----------------------");
			logger.info("Received " + order);
			logger.info("----------------------");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
