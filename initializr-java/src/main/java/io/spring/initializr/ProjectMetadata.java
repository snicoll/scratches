package io.spring.initializr;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
@ConfigurationProperties(prefix = "projects", ignoreUnknownFields = false)
public class ProjectMetadata {

	private final List<DependencyGroup> dependencies = new ArrayList<>();

	private final List<Type> types = new ArrayList<Type>();

	private final List<Packaging> packagings = new ArrayList<Packaging>();

	private final List<JavaVersion> javaVersions = new ArrayList<JavaVersion>();

	private final List<Language> languages = new ArrayList<Language>();

	private final List<BootVersion> bootVersions = new ArrayList<BootVersion>();

	public List<DependencyGroup> getDependencies() {
		return dependencies;
	}

	public List<Type> getTypes() {
		return types;
	}

	public List<Packaging> getPackagings() {
		return packagings;
	}

	public List<JavaVersion> getJavaVersions() {
		return javaVersions;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public List<BootVersion> getBootVersions() {
		return bootVersions;
	}

	/**
	 * Initialize and validate the configuration.
	 */
	@PostConstruct
	public void validate() {
		for (DependencyGroup group : dependencies) {
			for (Dependency dependency : group.getContent()) {
				validateDependency(dependency);
			}
		}
	}

	void validateDependency(Dependency dependency) {
		String id = dependency.getId();
		if (id == null) {
			if (!dependency.hasCoordinates()) {
				throw new InvalidProjectMetadataException("Invalid dependency, " +
						"should have at least an id or a groupId/artifactId pair.");
			}
			StringBuilder sb = new StringBuilder();
			sb.append(dependency.getGroupId()).append(":").append(dependency.getArtifactId());
			if (dependency.getVersion() != null) {
				sb.append(":").append(dependency.getVersion());
			}
			dependency.setId(sb.toString());
		}
		else if (!dependency.hasCoordinates()) {
			// Let's build the coordinates from the id
			StringTokenizer st = new StringTokenizer(id, ":");
			if (st.countTokens() < 2 || st.countTokens() > 3) {
				throw new InvalidProjectMetadataException("Invalid dependency, id should" +
						"have the form groupId:artifactId[:version] but got '" + id + "'");
			}
			dependency.setGroupId(st.nextToken());
			dependency.setArtifactId(st.nextToken());
			if (st.hasMoreTokens()) {
				dependency.setVersion(st.nextToken());
			}
		}

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class DependencyGroup {

		private String name;

		private String description;

		private final List<Dependency> content = new ArrayList<Dependency>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public List<Dependency> getContent() {
			return content;
		}
	}


	public static class Dependency extends IdentifiableElement {

		@JsonIgnore
		private String groupId;

		@JsonIgnore
		private String artifactId;

		@JsonIgnore
		private String version;

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getArtifactId() {
			return artifactId;
		}

		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		/**
		 * Specify if the dependency has its coordinates set, i.e. {@code groupId}
		 * and {@code artifactId}.
		 */
		public boolean hasCoordinates() {
			return groupId != null && artifactId != null;
		}
	}

	public static class Type extends DefaultIdentifiableElement {

		private String action;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
	}

	public static class Packaging extends DefaultIdentifiableElement {
	}


	public static class JavaVersion extends DefaultIdentifiableElement {
	}

	public static class Language extends DefaultIdentifiableElement {
	}

	public static class BootVersion extends DefaultIdentifiableElement {
	}


	public static class DefaultIdentifiableElement extends IdentifiableElement {

		private boolean defaultValue;

		public boolean isDefault() {
			return defaultValue;
		}

		public void setDefault(boolean defaultValue) {
			this.defaultValue = defaultValue;
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class IdentifiableElement {

		private String name;

		private String id;

		private String description;

		public String getName() {
			return (name != null ? name : getId());
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
