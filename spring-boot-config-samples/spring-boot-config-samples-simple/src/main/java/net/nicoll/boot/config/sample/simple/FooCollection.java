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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Stephane Nicoll
 */
public class FooCollection {

	private final List<String> items = new ArrayList<>();

	private Set<Integer> counters;

	public List<String> getItems() {
		return this.items;
	}

	public Set<Integer> getCounters() {
		return counters;
	}

	public void setCounters(Set<Integer> counters) {
		this.counters = counters;
	}
}
