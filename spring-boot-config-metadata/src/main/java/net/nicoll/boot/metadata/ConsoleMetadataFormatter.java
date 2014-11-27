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

package net.nicoll.boot.metadata;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.configurationmetadata.ConfigurationMetadataSource;
import org.springframework.util.StringUtils;

/**
 *
 * @author Stephane Nicoll
 */
public class ConsoleMetadataFormatter extends AbstractMetadataFormatter {

	@Override
	public String formatMetadata(ConfigurationMetadataRepository repository) {
		StringBuilder out = new StringBuilder();
		int noDotInDescription = 0;
		List<String> keysMissingDescription = new ArrayList<String>();
		List<ConfigurationMetadataGroup> groups = sortGroups(repository.getAllGroups().values());
		for (ConfigurationMetadataGroup group : groups) {
			out.append("========================================").append(NEW_LINE);
			StringBuilder sb = new StringBuilder();
			for (ConfigurationMetadataSource source : group.getSources().values()) {
				sb.append(source.getType()).append(" ");
			}
			out.append("Group --- ").append(group.getId()).append("(").append(sb.toString().trim()).append(")")
					.append(NEW_LINE).append("========================================").append(NEW_LINE);
			List<ConfigurationMetadataProperty> properties = sortProperties(group.getProperties().values());
			for (ConfigurationMetadataProperty property : properties) {
				StringBuilder item = new StringBuilder("-- " + property.getId() + " (" + property.getType() + ")");
				String description = property.getDescription();
				if (StringUtils.hasText(description)) {
					item.append(" - ");
					int dot = description.indexOf(".");
					if (dot != -1) {
						BreakIterator breakIterator = BreakIterator.getSentenceInstance();
						breakIterator.setText(description);
						item.append(description.substring(breakIterator.first(), breakIterator.next()));
					}
					else {
						item.append(description).append(" --- NO DOT FOUND!");
						noDotInDescription++;
					}
				}
				else {
					keysMissingDescription.add(property.getId());
				}
				out.append(item.toString()).append(NEW_LINE);
			}
		}
		out.append("-------- Stats --------").append(NEW_LINE).append("Not dot in description: ")
				.append(noDotInDescription).append(NEW_LINE).append("Missing description:").append(NEW_LINE);
		StringBuilder desc = new StringBuilder();
		for (String s : keysMissingDescription) {
			desc.append("\t").append(s).append("\n");
		}
		out.append(desc.toString());
		return out.toString();
	}
}
