package io.spring.initializr;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Stephane Nicoll
 */
public class ProjectMetadataTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	private final ProjectMetadata projectMetadata = new ProjectMetadata();

	@Test
	public void setCoordinatesFromId() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setId("org.foo:bar:1.2.3");
		projectMetadata.validateDependency(dependency);
		assertEquals("org.foo", dependency.getGroupId());
		assertEquals("bar", dependency.getArtifactId());
		assertEquals("1.2.3", dependency.getVersion());
	}

	@Test
	public void setCoordinatesFromIdNoVersion() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setId("org.foo:bar");
		projectMetadata.validateDependency(dependency);
		assertEquals("org.foo", dependency.getGroupId());
		assertEquals("bar", dependency.getArtifactId());
		assertNull(dependency.getVersion());
	}

	@Test
	public void setIdFromCoordinates() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setGroupId("org.foo");
		dependency.setArtifactId("bar");
		dependency.setVersion("1.0");
		projectMetadata.validateDependency(dependency);
		assertEquals("org.foo:bar:1.0", dependency.getId());
	}

	@Test
	public void setIdFromCoordinatesNoVersion() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setGroupId("org.foo");
		dependency.setArtifactId("bar");
		projectMetadata.validateDependency(dependency);
		assertEquals("org.foo:bar", dependency.getId());
	}

	@Test
	public void invalidDependency() {
		thrown.expect(InvalidProjectMetadataException.class);
		projectMetadata.validateDependency(new ProjectMetadata.Dependency());
	}

	@Test
	public void invalidIdFormatNoColon() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setId("myId");

		thrown.expect(InvalidProjectMetadataException.class);
		projectMetadata.validateDependency(dependency);
	}

	@Test
	public void invalidIdFormatTooManyColons() {
		ProjectMetadata.Dependency dependency = new ProjectMetadata.Dependency();
		dependency.setId("org.foo:bar:1.0:test:external");

		thrown.expect(InvalidProjectMetadataException.class);
		projectMetadata.validateDependency(dependency);
	}
}
