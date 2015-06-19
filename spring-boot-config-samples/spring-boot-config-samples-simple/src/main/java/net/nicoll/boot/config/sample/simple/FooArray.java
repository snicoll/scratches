/*
 * Copyright 2012-2015 the original author or authors.
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

package net.nicoll.boot.config.sample.simple;

/**
 *
 * @author Stephane Nicoll
 */
public class FooArray {

	private final String[] items = new String[3];

	private Integer[] counters;

	public String[] getItems() {
		return this.items;
	}

	public Integer[] getCounters() {
		return counters;
	}

	public void setCounters(Integer[] counters) {
		this.counters = counters;
	}
}
