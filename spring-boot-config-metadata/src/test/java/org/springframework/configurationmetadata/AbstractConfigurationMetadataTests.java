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

package org.springframework.configurationmetadata;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractConfigurationMetadataTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	protected void assertSource(ConfigurationMetadataSource actual, String groupId, String type, String sourceType) {
		assertNotNull(actual);
		assertEquals(groupId, actual.getGroupId());
		assertEquals(type, actual.getType());
		assertEquals(sourceType, actual.getSourceType());
	}

	protected void assertProperty(ConfigurationMetadataProperty actual, String id, String name,
			Class<?> type, Object defaultValue) {
		assertNotNull(actual);
		assertEquals(id, actual.getId());
		assertEquals(name, actual.getName());
		String typeName = type != null ? type.getName() : null;
		assertEquals(typeName, actual.getType());
		assertEquals(defaultValue, actual.getDefaultValue());
	}

	protected void assertItem(ConfigurationMetadataItem actual, String sourceType) {
		assertNotNull(actual);
		assertEquals(sourceType, actual.getSourceType());
	}

	protected InputStream getInputStreamFor(String name) throws IOException {
		Resource r = new ClassPathResource("metadata/configuration-metadata-" + name + ".json");
		return r.getInputStream();
	}
}
