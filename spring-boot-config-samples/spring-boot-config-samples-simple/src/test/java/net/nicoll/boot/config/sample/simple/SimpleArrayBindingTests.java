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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class SimpleArrayBindingTests extends AbstractBindingTests<FooArray> {

	@Override
	protected FooArray createInstance() {
		return new FooArray();
	}

	@Test
	public void arrayWithIndexes() {
		addKeys("foo.items[0]=0", "foo.items[2]=2");
		bind("foo");

		assertEquals("0", getTarget().getItems()[0]);
		assertEquals("2", getTarget().getItems()[2]);
		assertNull(getTarget().getItems()[1]);
	}

	@Test
	public void arrayWithCommaSeparated() {
		addKeys("foo.items=0,1,2");
		bind("foo");

		// TODO: There is no setter for the array, shouldn't we throw an exception instead?
		assertEquals(3, getTarget().getItems().length); ;
	}

	@Test
	public void arrayWithIndexOutOfBound() {
		addKeys("foo.items[5]=343");
		bind("foo");

		// TODO: we're out of bound and not setter exist to grow the array. shouldn't we throw an exception instead?
		assertEquals(3, getTarget().getItems().length);
	}

	@Test
	public void arrayWithSetterAndCommaSeparated() {
		addKeys("foo.counters=0,1,2");
		bind("foo");

		assertEquals(3, getTarget().getCounters().length);
		assertEquals(new Integer(0), getTarget().getCounters()[0]);
		assertEquals(new Integer(1), getTarget().getCounters()[1]);
		assertEquals(new Integer(2), getTarget().getCounters()[2]);
	}

}
