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

package net.nicoll.boot.metadata;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.util.StringUtils;

/**
 *
 * @author Stephane Nicoll
 */
public abstract class AbstractMetadataFormatter {

	protected static final String NEW_LINE = System.getProperty("line.separator");

	public static final Comparator<ConfigurationMetadataGroup> GROUP_COMPARATOR = new GroupComparator();

	public static final Comparator<ConfigurationMetadataProperty> PROPERTY_COMPARATOR = new PropertyComparator();


	protected String extractTagLine(ConfigurationMetadataProperty property, String defaultValue) {
		String description = property.getDescription();
		if (StringUtils.hasText(description)) {
			BreakIterator breakIterator = BreakIterator.getSentenceInstance();
			breakIterator.setText(description);
			return description.substring(breakIterator.first(), breakIterator.next());
		}
		return defaultValue;
	}


	protected List<ConfigurationMetadataGroup> sortGroups(Collection<ConfigurationMetadataGroup> groups) {
		List<ConfigurationMetadataGroup> result
				= new ArrayList<ConfigurationMetadataGroup>(groups);
		Collections.sort(result, GROUP_COMPARATOR);
		return result;
	}

	protected List<ConfigurationMetadataProperty> sortProperties(Collection<ConfigurationMetadataProperty> properties) {
		List<ConfigurationMetadataProperty> result =
				new ArrayList<ConfigurationMetadataProperty>(properties);
		Collections.sort(result, PROPERTY_COMPARATOR);
		return result;
	}

	private static class GroupComparator implements Comparator<ConfigurationMetadataGroup> {

		@Override
		public int compare(ConfigurationMetadataGroup o1, ConfigurationMetadataGroup o2) {
			if (ConfigurationMetadataRepository.ROOT_GROUP.equals(o1.getId())) {
				return -1;
			}
			if (ConfigurationMetadataRepository.ROOT_GROUP.equals(o2.getId())) {
				return 1;
			}
			return o1.getId().compareTo(o2.getId());
		}
	}

	private static class PropertyComparator implements Comparator<ConfigurationMetadataProperty> {
		@Override
		public int compare(ConfigurationMetadataProperty o1, ConfigurationMetadataProperty o2) {
			return o1.getId().compareTo(o2.getId());
		}
	}

}
