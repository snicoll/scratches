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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.nicoll.boot.metadata.ConsoleMetadataFormatter;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;

/**
 *
 * @author Stephane Nicoll
 */
public class ConsoleConfigDiffFormatter extends AbstractConfigDiffFormatter {

	@Override
	public String formatDiff(ConfigDiffResult result) throws IOException {
		StringBuilder out = new StringBuilder();
		out.append("===========================================================================").append(NEW_LINE);
		out.append("Config meta-data diff between '").append(result.getLeftVersion()).append("' and '")
				.append(result.getRightVersion()).append("'").append(NEW_LINE);
		outputGroups(out, result, true);
		outputGroups(out, result, false);
		outputModifiedGroups(out, result);
		outputProperties(out, result, true);
		outputProperties(out, result, false);
		out.append("===========================================================================").append(NEW_LINE);


		return out.toString();
	}

	private void outputGroups(StringBuilder out, ConfigDiffResult result, boolean added) {
		out.append("===========================================================================").append(NEW_LINE);
		List<ConfigDiffEntry<ConfigurationMetadataGroup>> groups =
				sortGroups(result.getGroupsDiffFor(added ? ConfigDiffType.ADD : ConfigDiffType.DELETE), !added);
		out.append("Groups ").append(added ? "added" : "removed").append(" (").
				append(groups.size()).append("):").append(NEW_LINE);
		out.append(NEW_LINE);
		for (ConfigDiffEntry<ConfigurationMetadataGroup> diff : groups) {
			ConfigurationMetadataGroup group = added ? diff.getRight() : diff.getLeft();
			int size = group.getProperties().size();
			out.append(getGroupId(group)).append(" (").append(added ? "+" : "-").append(
					propertiesCount(size)).append(")").append(NEW_LINE);
		}
	}

	private void outputProperties(StringBuilder out, ConfigDiffResult result, boolean added) {
		out.append("===========================================================================").append(NEW_LINE);
		List<ConfigDiffEntry<ConfigurationMetadataProperty>> properties =
				sortProperties(result.getPropertiesDiffFor(added ? ConfigDiffType.ADD : ConfigDiffType.DELETE), !added);
		out.append("Properties ").append(added ? "added" : "removed").append(" (")
				.append(properties.size()).append("):").append(NEW_LINE);
		out.append(NEW_LINE);
		for (ConfigDiffEntry<ConfigurationMetadataProperty> diff : properties) {
			ConfigurationMetadataProperty property = (added ? diff.getRight() : diff.getLeft());
			out.append(ConsoleMetadataFormatter.formatProperty(property)).append(NEW_LINE);
		}
	}

	private void outputModifiedGroups(StringBuilder out, ConfigDiffResult result) {
		out.append("===========================================================================").append(NEW_LINE);
		List<ConfigDiffEntry<ConfigurationMetadataGroup>> groups =
				sortGroups(result.getGroupsDiffFor(ConfigDiffType.MODIFY), true);
		out.append("Groups modified (").
				append(groups.size()).append("):").append(NEW_LINE);
		out.append(NEW_LINE);
		for (ConfigDiffEntry<ConfigurationMetadataGroup> diff : groups) {
			outputModifiedGroup(out, diff);
		}
	}


	private void outputModifiedGroup(StringBuilder out, ConfigDiffEntry<ConfigurationMetadataGroup> diff) {
		Collection<String> deleted = new ArrayList<String>();
		Collection<String> added = new ArrayList<String>();
		Map<String, ConfigurationMetadataProperty> leftProperties = diff.getLeft().getProperties();
		Map<String, ConfigurationMetadataProperty> rightProperties = diff.getRight().getProperties();
		for (String key : leftProperties.keySet()) {
			if (!rightProperties.containsKey(key)) {
				deleted.add(key);
			}
		}
		for (String key : rightProperties.keySet()) {
			if (!leftProperties.containsKey(key)) {
				added.add(key);
			}
		}
		out.append(getGroupId(diff.getLeft())).append(" (+").append(propertiesCount(
				added.size())).append(" -").append(propertiesCount(deleted.size())).append(")").append(NEW_LINE);

	}

	private String getGroupId(ConfigurationMetadataGroup group) {
		String id = group.getId();
		return (id.equals("_ROOT_GROUP_") ? "(root)" : id);
	}

	private String propertiesCount(int size) {
		return size + (size > 1 ? " properties" : " property");
	}

}
