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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.configurationmetadata.ConfigurationMetadataGroup;
import org.springframework.configurationmetadata.ConfigurationMetadataProperty;
import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.configurationmetadata.ConfigurationMetadataRepositoryJsonLoader;
import org.springframework.configurationmetadata.ConfigurationMetadataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 *
 * @author Stephane Nicoll
 */
public class Sample {

	public static void main(String[] args) throws IOException {
		ConfigurationMetadataRepositoryJsonLoader loader = new ConfigurationMetadataRepositoryJsonLoader();
		ConfigurationMetadataRepository repo = loader.loadAll(getResources());
		for (ConfigurationMetadataGroup group : repo.getAllGroups().values()) {
			System.out.println("========================================");
			StringBuilder sb = new StringBuilder();
			for (ConfigurationMetadataSource source : group.getSources().values()) {
				sb.append(source.getType()).append(" ");
			}
			System.out.println("Group --- " + group.getId() + "(" + sb.toString().trim() + ")");
			System.out.println("========================================");
			for (ConfigurationMetadataProperty property : group.getProperties().values()) {
				System.out.println("-- " + property.getId() + " (" + property.getType() + ")");
			}
		}

	}


	private static List<InputStream> getResources() throws IOException {
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources("classpath*:/META-INF/spring-configuration-metadata.json");
		List<InputStream> result = new ArrayList<InputStream>();
		for (Resource resource : resources) {
			result.add(resource.getInputStream());
		}
		return result;
	}
}
