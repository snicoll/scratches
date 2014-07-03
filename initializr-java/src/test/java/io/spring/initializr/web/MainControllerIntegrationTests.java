package io.spring.initializr.web;

import io.spring.initializr.TestApp;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApp.class)
@WebAppConfiguration
@ActiveProfiles("test-full")
@IntegrationTest("server.port=0")
public class MainControllerIntegrationTests {

	@Value("${local.server.port}")
	private int port;

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void validateProjectMetadata() {
		String url = createUrl("/");
		String json = restTemplate.getForObject(url, String.class);
		JsonAssert rootAssert = new JsonAssert(json);

		rootAssert.assertRootSize("dependencies", 2);
		JsonAssert dependenciesAssert = rootAssert.getChild("dependencies");
		dependenciesAssert.assertArraySize(2); // 2 groups


		JsonAssert coreGroupAssert = dependenciesAssert.getElement(0);
		coreGroupAssert.assertField("name", "Core");
		coreGroupAssert.assertField("description", "The core dependencies");
		coreGroupAssert.assertRootSize("content", 1);
		JsonAssert webDependency = coreGroupAssert.getChild("content").getElement(0);
		assertDependency(webDependency, "org.springframework.boot:spring-boot-starter-web",
				"Web","Necessary infrastructure to build a REST service");


		JsonAssert otherGroupAssert = dependenciesAssert.getElement(1);
		otherGroupAssert.assertField("name", "Other");
		otherGroupAssert.hasNoField("description");
		otherGroupAssert.assertRootSize("content", 2);
		JsonAssert otherDependencies = otherGroupAssert.getChild("content");
		assertDependency(otherDependencies.getElement(0), "org.acme:foo:1.3.5", "Foo", null);
		assertDependency(otherDependencies.getElement(1), "org.acme:bar:2.1.0", "Bar", null);

		rootAssert.assertRootSize("types", 4);
		rootAssert.assertRootSize("packagings", 2);
		rootAssert.assertRootSize("javaVersions", 3);
		rootAssert.assertRootSize("languages", 2);
		rootAssert.assertRootSize("bootVersions", 3);
	}

	private void assertDependency(JsonAssert jsonAssert, String id, String name, String description) {
		jsonAssert.assertField("id", id);
		jsonAssert.assertField("name", name);
		if (description != null) {
			jsonAssert.assertField("description", description);
		}
		else {
			jsonAssert.hasNoField("description");
		}
		jsonAssert.hasNoField("groupId", "artifactId", "version");
	}

	private String createUrl(String context) {
		return "http://localhost:" + port + context;
	}

}
