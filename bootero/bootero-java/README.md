This is a follow-up on the simple groovy introduction for Java that illustrates
how one can get started easily using <http://start.spring.io>

# Creating a skeleton project

Go to <http://start.spring.io>, select "web" and click on `generate`. This should
download a zip file with the project. Open the project in our IDE and switch the
version to java 1.8 as we want to use the latest java features.

# Adding our controller

We can now create a controller that looks identical to the one we wrote with
Groovy. Because the template has added `@ComponentScan` we only need to
annotate our rest controller with `@RestController` and it's picked up
automatically since it is in the same package space.

# Testing our controller

We're going to test it using the new `@IntegrationTest` annotation that
configures the server to use a random port. We also inject it in the test.

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class HeroControllerIntegrationTest {

	@Value("${local.server.port}")
	private int port;
}
```

Then we use `RestTemplate` to get the body of the home URL which is our
hello world.

