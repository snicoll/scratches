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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.nicoll.boot.metadata.AbstractMetadataFormatter;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;

/**
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractConfigDiffFormatter extends AbstractMetadataFormatter implements ConfigDiffFormatter {


	protected List<ConfigDiffEntry<ConfigurationMetadataGroup>> sortGroups(
			List<ConfigDiffEntry<ConfigurationMetadataGroup>> groups, final boolean useLeft) {
		List<ConfigDiffEntry<ConfigurationMetadataGroup>> result =
				new ArrayList<ConfigDiffEntry<ConfigurationMetadataGroup>>(groups);
		Collections.sort(result, new Comparator<ConfigDiffEntry<ConfigurationMetadataGroup>>() {
			@Override
			public int compare(ConfigDiffEntry<ConfigurationMetadataGroup> o1, ConfigDiffEntry<ConfigurationMetadataGroup> o2) {
				ConfigurationMetadataGroup first = (useLeft ? o1.getLeft() : o1.getRight());
				ConfigurationMetadataGroup second = (useLeft ? o2.getLeft() : o2.getRight());
				return GROUP_COMPARATOR.compare(first, second);
			}
		});
		return result;
	}

	protected List<ConfigDiffEntry<ConfigurationMetadataProperty>> sortProperties(
			List<ConfigDiffEntry<ConfigurationMetadataProperty>> groups, final boolean useLeft) {
		List<ConfigDiffEntry<ConfigurationMetadataProperty>> result =
				new ArrayList<ConfigDiffEntry<ConfigurationMetadataProperty>>(groups);
		Collections.sort(result, new Comparator<ConfigDiffEntry<ConfigurationMetadataProperty>>() {
			@Override
			public int compare(ConfigDiffEntry<ConfigurationMetadataProperty> o1, ConfigDiffEntry<ConfigurationMetadataProperty> o2) {
				ConfigurationMetadataProperty first = (useLeft ? o1.getLeft() : o1.getRight());
				ConfigurationMetadataProperty second = (useLeft ? o2.getLeft() : o2.getRight());
				return PROPERTY_COMPARATOR.compare(first, second);
			}
		});
		return result;
	}

}
