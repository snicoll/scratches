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

import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import static org.junit.Assert.*;

/**
 *
 * @author Stephane Nicoll
 */
public class SimpleBindingTests {

	private final ConfigurableEnvironment environment = new StandardEnvironment();

	private final Foo target = new Foo();

	@Test
	public void bindingSimpleInteger() {
		EnvironmentTestUtils.addEnvironment(environment, "foo.port=7070");
		bind("foo");
		assertEquals(7070, target.getPort());
	}


	@Test
	public void bindingWithNonPublicSetter() {
		target.setId("test");
		EnvironmentTestUtils.addEnvironment(environment, "foo.id=abc");
		bind("foo");
		assertEquals("should not have changed", "test", target.getId());
	}

	private void bind(String prefix) {
		new RelaxedDataBinder(target, prefix).bind(
				new PropertySourcesPropertyValues(environment.getPropertySources()));
	}

}
