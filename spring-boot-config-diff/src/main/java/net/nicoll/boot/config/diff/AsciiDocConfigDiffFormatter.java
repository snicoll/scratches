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
import java.util.List;

import net.nicoll.boot.metadata.ConsoleMetadataFormatter;

import org.springframework.configurationmetadata.ConfigurationMetadataProperty;

/**
 *
 * @author Stephane Nicoll
 */
public class AsciiDocConfigDiffFormatter extends AbstractConfigDiffFormatter {

	@Override
	public String formatDiff(ConfigDiffResult result) throws IOException {
		StringBuilder out = new StringBuilder();
		out.append("Configuration properties change between `").append(result.getLeftVersion())
				.append("` and `").append(result.getRightVersion()).append("`").append(NEW_LINE);
		out.append(NEW_LINE);
		out.append("The following keys were **added**:").append(NEW_LINE);
		out.append(NEW_LINE);
		out.append(".New keys in `").append(result.getRightVersion()).append("`").append(NEW_LINE);
		appendProperties(out, result, true);
		out.append(NEW_LINE);
		out.append("The following keys were **removed**:").append(NEW_LINE);
		out.append(NEW_LINE);
		out.append(".Removed keys in `").append(result.getRightVersion()).append("`").append(NEW_LINE);
		appendProperties(out, result, false);
		return out.toString();
	}

	private void appendProperties(StringBuilder out, ConfigDiffResult result, boolean added) {
		List<ConfigDiffEntry<ConfigurationMetadataProperty>> properties =
				sortProperties(result.getPropertiesDiffFor(added ? ConfigDiffType.ADD : ConfigDiffType.DELETE), !added);
		out.append("|======================").append(NEW_LINE);
		out.append("|Key  |Default value |Description").append(NEW_LINE);
		for (ConfigDiffEntry<ConfigurationMetadataProperty> diff : properties) {
			ConfigurationMetadataProperty property = (added ? diff.getRight() : diff.getLeft());
			// |`spring.foo` | | Bla bla bla
			out.append("|`").append(property.getId()).append("` |");
			if (property.getDefaultValue() != null) {
				out.append("`").append(ConsoleMetadataFormatter
						.defaultValueToString(property.getDefaultValue())).append("`");
			}
			out.append(" |");
			if (property.getDescription() != null) {
				out.append(ConsoleMetadataFormatter.descriptionToTagLine(property.getDescription()));
			}
			out.append(NEW_LINE);
		}
		out.append("|======================").append(NEW_LINE);
	}
}
