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

package net.nicoll.boot.config.diff.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.configurationmetadata.ConfigurationMetadataRepository;
import org.springframework.configurationmetadata.ConfigurationMetadataRepositoryJsonBuilder;
import org.springframework.core.io.Resource;

/**
 *
 * @author Stephane Nicoll
 */
public class ConfigurationMetadataRepositoryLoader {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationMetadataRepositoryLoader.class);


	private final AetherDependencyResolver dependencyResolver;

	public ConfigurationMetadataRepositoryLoader(AetherDependencyResolver dependencyResolver) {
		this.dependencyResolver = dependencyResolver;
	}

	public ConfigurationMetadataRepository load(String version) throws IOException {
		ConfigurationMetadataRepositoryJsonBuilder builder = ConfigurationMetadataRepositoryJsonBuilder.create();
		load(builder, "org.springframework.boot:spring-boot:" + version, true);
		load(builder, "org.springframework.boot:spring-boot-actuator:" + version, true);
		load(builder, "org.springframework.boot:spring-boot-autoconfigure:" + version, true);
		load(builder, "org.springframework.boot:spring-boot-devtools:" + version, false);
		return builder.build();
	}

	private Resource load(ConfigurationMetadataRepositoryJsonBuilder builder, String coordinates, boolean mandatory)
			throws IOException {
		try {
			ArtifactResult artifactResult = dependencyResolver.resolveDependency(coordinates);
			File file = artifactResult.getArtifact().getFile();
			InputStream stream = new URLClassLoader(new URL[] {file.toURI().toURL()})
					.getResourceAsStream("META-INF/spring-configuration-metadata.json");
			if (stream != null) {
				try {
					logger.info("Adding meta-data from '" + coordinates + "'");
					builder.withJsonResource(stream);
				}
				finally {
					stream.close();
				}
			}
			else {
				logger.info("No meta-data found for '" + coordinates + "'");
			}
		}
		catch (ArtifactResolutionException e) {
			if (mandatory) {
				throw new IllegalStateException("Could not load " + coordinates, e);
			}
			logger.info("Ignoring '" + coordinates + " (not found)");
		}
		return null;
	}
}
