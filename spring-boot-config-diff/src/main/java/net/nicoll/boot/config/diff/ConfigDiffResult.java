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

package net.nicoll.boot.config.diff;

import java.util.Collections;
import java.util.List;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigDiffResult {


	private final String leftVersion;

	private final String rightVersion;

	private final MultiValueMap<ConfigDiffType, ConfigDiffEntry<ConfigurationMetadataGroup>> groups =
			new LinkedMultiValueMap<ConfigDiffType, ConfigDiffEntry<ConfigurationMetadataGroup>>();

	private final MultiValueMap<ConfigDiffType, ConfigDiffEntry<ConfigurationMetadataProperty>> properties =
			new LinkedMultiValueMap<ConfigDiffType, ConfigDiffEntry<ConfigurationMetadataProperty>>();

	public ConfigDiffResult(String leftVersion, String rightVersion) {
		this.leftVersion = leftVersion;
		this.rightVersion = rightVersion;
	}

	public String getLeftVersion() {
		return leftVersion;
	}

	public String getRightVersion() {
		return rightVersion;
	}

	public List<ConfigDiffEntry<ConfigurationMetadataGroup>> getGroupsDiffFor(ConfigDiffType type) {
		List<ConfigDiffEntry<ConfigurationMetadataGroup>> content = this.groups.get(type);
		if (content == null) {
			return Collections.emptyList();
		}
		return content;
	}

	public List<ConfigDiffEntry<ConfigurationMetadataProperty>> getPropertiesDiffFor(ConfigDiffType type) {
		List<ConfigDiffEntry<ConfigurationMetadataProperty>> content = this.properties.get(type);
		if (content == null) {
			return Collections.emptyList();
		}
		return content;
	}

	void register(ConfigDiffType type, ConfigurationMetadataGroup left, ConfigurationMetadataGroup right) {
		this.groups.add(type, new ConfigDiffEntry<ConfigurationMetadataGroup>(left, right));
	}

	void register(ConfigDiffType type, ConfigurationMetadataProperty left, ConfigurationMetadataProperty right) {
		this.properties.add(type, new ConfigDiffEntry<ConfigurationMetadataProperty>(left, right));
	}

}
