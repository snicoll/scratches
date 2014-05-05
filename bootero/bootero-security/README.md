This demo is going to secure the endpoints that we have defined so far and showcase what boot does
by default and how to configure it

# Updating our project

Based on the `bootero-data-rest`, we first need to add the starter project
to enable Spring security:

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

# Defaulting

If we run our `HeroControllerIntegrationTest` again, it will fail because adding spring
security has enabled basic authentication for our endpoints. Spring boot has generated
a default password for our application when it starts.

We can show that by updating our test to inject the `SecurityProperties` and switch
our `RestTemplate` to `TestRestTemplate`, taking the user name and password of the
`User` instance, something like

```java
@Autowired private SecurityProperties securityProperties;

@Test
public void runAndTestHttpEndpoint() {
    String url = "http://localhost:" + this.port + "/";
    String body = new TestRestTemplate(securityProperties.getUser().getName(),
            securityProperties.getUser().getPassword()).getForObject(url, String.class);
    assertEquals("Hello World!", body);
}
```

You can just as well disable that behavior altogether by adding `security.basic.enabled=false` in
your configuration. You can also replace boot's default behaviour by adding `@EnableWebSecurity`
to any configuration class.

# Customizing authentication manager and role

But let's instead add a custom `AuthenticationManager` with a specific role to be able to look
for user by last name.

First, let's create two users, one with the roles `HERO` and `USER` and one with the
role `USER` only:

```java
@Configuration
static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("hero").password("hero").roles("HERO", "USER").and()
                .withUser("user").password("user").roles("USER");
    }
}
```

Then add the necessary role to our method and enable method security, that is `@Secured("ROLE_HERO")` on
the method and `@EnableGlobalMethodSecurity(securedEnabled = true)` on the config class. Let's curl
again on the URL to show the result.

# Current issues/TODO

* Better understand what `EnableGlobalMethodSecurity` does
* Why the name of the role must be prefixed by `ROLE` (i.e. what are the other options?)
