This demo is based on `bootero-mongo` and demonstrates how the repository that we
have created can easily be exported as a rest endpoint.

# Updating our project

Based on the `bootero-mongo`, we first need to add the starter project to enable
Spring data rest:

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

# Updating our repository

To expose our `UserAccountRepository` to be exposed as a rest resource, we only
need to annotate it with `RepositoryRestResource`. Exposing those services requires
to import the `RepositoryRestMvcConfiguration` so adding an `@Import` to our
configuration class should do the trick.

Our service now has the following definition:

```java
@RepositoryRestResource(path = "users")
public interface UserAccountRepository extends MongoRepository<UserAccount, Long> {
```

We also want to expose our repositories to a custom context path. For that, we need
to customize the config. Let's add the following config to our Application

```java
@Configuration
public static class RepositoryRestMvnConfig extends RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setBaseUri(URI.create("repo"));
	}
}
```

# Testing the service

The last run of our tests should have stored a few accounts in our mongo store. Let's
use curl to hit our service `curl http://localhost:8080/repo/users`. From there we can browse
resources easily.

# Customizing a finder

If time permits, we can customize the way a finder method is exposed. For instance,
finders are exposed according to the name of the method. We can change that easily:

```java
@RestResource(path = "username")
UserAccount findByUsername(@Param("name") String username);
```

