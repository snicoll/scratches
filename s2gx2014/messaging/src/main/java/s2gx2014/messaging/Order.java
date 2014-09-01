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
import java.util.Date;

@SuppressWarnings("serial")
public class Order implements Serializable {

	private String id;

	private Date date;

	private String customerId;

	public Order() {
	}

	public Order(String id, Date date, String customerId) {
		this.id = id;
		this.date = date;
		this.customerId = customerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Order{");
		sb.append("id='").append(id).append('\'');
		sb.append(", date=").append(date);
		sb.append(", customerId='").append(customerId).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
