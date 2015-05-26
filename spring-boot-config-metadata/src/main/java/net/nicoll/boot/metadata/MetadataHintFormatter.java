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

import java.io.IOException;

import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.util.StringUtils;

/**
 *
 * @author Stephane Nicoll
 */
public class MetadataHintFormatter extends AbstractMetadataFormatter {

	@Override
	public String formatMetadata(ConfigurationMetadataRepository repository) throws IOException {
		StringBuilder out = new StringBuilder();
		for (ConfigurationMetadataProperty property : repository.getAllProperties().values()) {
			if (hasDocumentationHints(property)) {
				out.append("Hints - ").append(property.getId()).append(" (")
						.append(property.getDescription()).append(")").append(NEW_LINE);
			}
		}
		return out.toString();
	}

	private boolean hasDocumentationHints(ConfigurationMetadataProperty property) {
		String description = property.getDescription();
		if (!StringUtils.hasText(description)) {
			return false;
		}
		String content = description.toLowerCase();
		if (content.contains("possible values") || content.contains("can be")) {
			return true;
		}
		return false;
	}
}
