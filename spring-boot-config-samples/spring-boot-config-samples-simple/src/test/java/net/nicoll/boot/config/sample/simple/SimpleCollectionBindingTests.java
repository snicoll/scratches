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

import java.util.Iterator;

import org.junit.Test;

import org.springframework.beans.InvalidPropertyException;

import static org.junit.Assert.*;

/**
 * @author Stephane Nicoll
 */
public class SimpleCollectionBindingTests extends AbstractBindingTests<FooCollection> {

	@Override
	protected FooCollection createInstance() {
		return new FooCollection();
	}

	@Test
	public void listWithIndexes() {
		addKeys("foo.items[0]=0", "foo.items[2]=2");
		bind("foo");

		assertEquals("0", getTarget().getItems().get(0));
		assertEquals("2", getTarget().getItems().get(2));
		assertNull(getTarget().getItems().get(1));
	}

	@Test
	public void listWithCommaSeparated() {
		addKeys("foo.items=0,1,2");
		bind("foo");

		// TODO: There is no setter for the list, shouldn't we throw an exception instead?
		assertEquals(0, getTarget().getItems().size());;
	}

	@Test
	public void setWithCommaSeparated() {
		addKeys("foo.counters=0,1,2");
		bind("foo");

		assertEquals(3, getTarget().getCounters().size());
		Iterator<Integer> iterator = getTarget().getCounters().iterator();
		assertEquals(new Integer(0), iterator.next());;
		assertEquals(new Integer(1), iterator.next());;
		assertEquals(new Integer(2), iterator.next());;
	}

	@Test
	public void setWithIndex() {
		addKeys("foo.counters[1]=1");

		thrown.expect(InvalidPropertyException.class);
		bind("foo");

	}
}
