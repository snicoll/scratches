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

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author Stephane Nicoll
 */
public abstract class AbstractBindingTests<T> {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	private final ConfigurableEnvironment environment = new StandardEnvironment();

	private T target;

	@Before
	public void setUp() {
		this.target = createInstance();
	}

	protected abstract T createInstance();

	public ConfigurableEnvironment getEnvironment() {
		return environment;
	}

	public T getTarget() {
		return target;
	}

	protected void addKeys(String...pairs) {
		EnvironmentTestUtils.addEnvironment(this.environment, pairs);
	}

	protected void bind(String prefix) {
		RelaxedDataBinder relaxedDataBinder = new RelaxedDataBinder(this.target, prefix);
		relaxedDataBinder.setAutoGrowNestedPaths(true);
		relaxedDataBinder.setConversionService(new DefaultConversionService());
		relaxedDataBinder.bind(
				new PropertySourcesPropertyValues(this.environment.getPropertySources()));
	}
}
