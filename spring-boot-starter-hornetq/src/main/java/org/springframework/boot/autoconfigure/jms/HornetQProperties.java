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

package org.springframework.boot.autoconfigure.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for HornetQ
 *
 * @author Stephane Nicoll
 * @since 1.1
 */
@ConfigurationProperties(prefix = "spring.hornetq")
public class HornetQProperties {

	private HornetQMode mode = HornetQMode.NATIVE;

	private String host = "localhost";

	private int port = 5445;

	private final Embedded embedded = new Embedded();

	public HornetQMode getMode() {
		return mode;
	}

	public void setMode(HornetQMode mode) {
		this.mode = mode;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Embedded getEmbedded() {
		return embedded;
	}

	public static class Embedded {

		private String[] queues = new String[0];

		private String[] topics = new String[0];

		public String[] getQueues() {
			return queues;
		}

		public void setQueues(String[] queues) {
			this.queues = queues;
		}

		public String[] getTopics() {
			return topics;
		}

		public void setTopics(String[] topics) {
			this.topics = topics;
		}
	}

}
