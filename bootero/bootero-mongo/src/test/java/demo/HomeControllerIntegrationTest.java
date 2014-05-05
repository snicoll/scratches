package demo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class HomeControllerIntegrationTest {

	@Value("${local.server.port}")
	private int port;

	@Test
	public void runAndTestHttpEndpoint() {
		String url = "http://localhost:" + this.port + "/";
		String body = new RestTemplate().getForObject(url, String.class);
		assertEquals("Hello World!", body);
	}

}
