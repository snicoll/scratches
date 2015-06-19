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
 * @author Stephane Nicoll
 */
public class SimpleSingleValueBindingTests extends AbstractBindingTests<FooSingleValue> {

	@Override
	protected FooSingleValue createInstance() {
		return new FooSingleValue();
	}

	@Test
	public void bindingSimpleInteger() {
		addKeys("foo.port=7070");
		bind("foo");

		assertEquals(7070, getTarget().getPort());
	}

	@Test
	public void bindingWithNonPublicSetter() {
		getTarget().setId("test");
		addKeys("foo.id=abc");
		bind("foo");

		assertEquals("should not have changed", "test", getTarget().getId());
	}

}
