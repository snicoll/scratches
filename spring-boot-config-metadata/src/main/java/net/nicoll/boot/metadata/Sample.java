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

import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.configurationmetadata.ConfigurationMetadataRepositoryJsonLoader;
import org.springframework.configurationmetadata.ConfigurationMetadataRepositoryJsonLoader.InputSource;
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
		System.out.println(getMetadataFormatter().formatMetadata(repo));
	}

	private static MetadataFormatter getMetadataFormatter() {
		return new ConsoleMetadataFormatter();
		//return new CsvMetadataFormatter();
	}

	private static List<InputSource> getResources() throws IOException {
		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources("classpath*:/META-INF/spring-configuration-metadata.json");
		List<InputSource> result = new ArrayList<InputSource>();
		for (final Resource resource : resources) {
			result.add(new InputSource() {
				public InputStream open() throws IOException {
					return resource.getInputStream();
				}
			});
		}
		return result;
	}

}
