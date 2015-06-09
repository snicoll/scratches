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
import java.util.Arrays;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorPolicy;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;

/**
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class AetherDependencyResolver {

	public static final RemoteRepository SPRING_IO_RELEASE = new RemoteRepository.Builder("release",
			"default", "https://repo.spring.io/release").build();

	public static final RemoteRepository SPRING_IO_MILESTONE = new RemoteRepository.Builder("milestone",
			"default", "https://repo.spring.io/milestone").build();

	public static final RemoteRepository SPRING_IO_SNAPSHOT = new RemoteRepository.Builder("snapshot",
			"default", "https://repo.spring.io/snapshot").build();

	private final RepositorySystem repositorySystem;

	private final RepositorySystemSession session;

	private final List<RemoteRepository> repositories;

	public AetherDependencyResolver(RemoteRepository... remoteRepositories) throws Exception {
		ServiceLocator serviceLocator = createServiceLocator();
		this.repositorySystem = createRepositorySystem(serviceLocator);
		this.session = createRepositorySystemSession(this.repositorySystem);
		this.repositories = Arrays.asList(remoteRepositories);
	}

	public static AetherDependencyResolver withAllRepositories() throws Exception {
		return new AetherDependencyResolver(SPRING_IO_RELEASE, SPRING_IO_MILESTONE, SPRING_IO_SNAPSHOT);
	}

	public ArtifactResult resolveDependency(String dependency) throws ArtifactResolutionException {
		Artifact artifact = new DefaultArtifact(dependency);
		ArtifactRequest request =
				new ArtifactRequest(artifact, this.repositories, null);
		return this.repositorySystem.resolveArtifact(session, request);
	}

	private static RepositorySystem createRepositorySystem(ServiceLocator serviceLocator) throws Exception {
		return serviceLocator.getService(RepositorySystem.class);
	}

	private static RepositorySystemSession createRepositorySystemSession(RepositorySystem repositorySystem) throws Exception {
		DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

		LocalRepository localRepository = new LocalRepository(getM2RepoDirectory());
		LocalRepositoryManager localRepositoryManager = repositorySystem
				.newLocalRepositoryManager(session, localRepository);
		session.setLocalRepositoryManager(localRepositoryManager);

		session.setArtifactDescriptorPolicy(new SimpleArtifactDescriptorPolicy(
				ArtifactDescriptorPolicy.STRICT));

		return session;
	}

	private static ServiceLocator createServiceLocator() {
		DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
		locator.addService(RepositorySystem.class, DefaultRepositorySystem.class);
		locator.addService(RepositoryConnectorFactory.class,
				BasicRepositoryConnectorFactory.class);
		locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
		locator.addService(TransporterFactory.class, FileTransporterFactory.class);
		return locator;
	}

	private static File getM2RepoDirectory() {
		return new File(getMavenHome(), "repository");
	}

	private static File getMavenHome() {
		return new File(System.getProperty("user.home"), ".m2");
	}


	public static void main(String[] args) throws Exception {
		System.out.println(AetherDependencyResolver.withAllRepositories()
				.resolveDependency("org.springframework.boot:spring-boot:1.2.6.RELEASE"));
	}
}