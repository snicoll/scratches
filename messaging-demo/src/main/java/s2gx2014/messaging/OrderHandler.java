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

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author Stephane Nicoll
 */
@Component
public class OrderHandler {

	private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

	@JmsListener(destination = "orderRequest")
	public OrderStatus processOrder(Order order, @Header String orderType) {
		logger.info("Processing " + order);
		// processing
		return new OrderStatus(order, "orderType=" + orderType + " - All good, bro!");

	}
}
