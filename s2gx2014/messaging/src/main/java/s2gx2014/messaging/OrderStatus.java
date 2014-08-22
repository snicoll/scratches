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

import java.io.Serializable;

/**
 * @author Stephane Nicoll
 */
@SuppressWarnings("serial")
public class OrderStatus implements Serializable {

	private String orderId;

	private String customerId;

	private String text;

	public OrderStatus() {
	}

	public OrderStatus(Order order, String text) {
		this.orderId = order.getId();
		this.customerId = order.getCustomerId();
		this.text = text;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("OrderStatus{");
		sb.append("orderId='").append(orderId).append('\'');
		sb.append(", customerId='").append(customerId).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
