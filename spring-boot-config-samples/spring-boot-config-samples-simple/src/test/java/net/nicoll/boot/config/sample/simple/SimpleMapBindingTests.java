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
public class SimpleMapBindingTests extends AbstractBindingTests<FooMap> {

	@Override
	protected FooMap createInstance() {
		return new FooMap();
	}

	@Test
	public void mapWithDotNotation() {
		addKeys("foo.items.one=1");
		bind("foo");
		assertEquals(new Integer(1), getTarget().getItems().get("one"));
		assertEquals(1, getTarget().getItems().size());
	}

	@Test
	public void mapWithBracketNotation() {
		addKeys("foo.items[one]=1");
		bind("foo");
		assertEquals(new Integer(1), getTarget().getItems().get("one"));
		assertEquals(1, getTarget().getItems().size());
	}

	@Test
	public void mapWithBracketNotationAndDotInKey() {
		addKeys("foo.items[one.key]=1");
		bind("foo");
		assertEquals(new Integer(1), getTarget().getItems().get("one.key"));
		assertEquals(1, getTarget().getItems().size());
	}
}
