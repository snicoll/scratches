This demo is going to show how boot can add production-ready features to an
application easily. We'll focus here on monitoring and other administrative
related services

# Updating our project

Based on the `bootero-data-security`, we first need to add the starter project
to enable these services:

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

# Out-of-the-box endpoints

Because Spring Security is on the classpath, the endpoints have been automatically
secured. The `ADMIN` role is required by default, so let's update the property
to require the `HERO` role instead:

```
management.security.role=HERO
```

We can then browse a few endpoints with our `hero` user to show what happened by
simply adding that dependency:

* <http://localhost:8080/env> provides some info regarding the environment and
a useful endpoint to get a value for a particular property. Let's try for
instance <http://localhost:8080/env/java.version>
* <http://localhost:8080/beans> shows the list of beans (both the ones defined
explicitly and the ones created by boot through the auto-configuration mechanism).
* <http://localhost:8080/autoconfig> gives a list of what matched and what
did not. Very useful to better understand why a certain behaviour was enabled

# Customizing the health indicator

<http://localhost:8080/health> is a health monitoring which can be easily
customized by defining a `HealthIndicator` bean. Something like:

```java
@Bean
HealthIndicator<Map<String,Object>> health() {
    return () -> {
        Map<String, Object> result = new HashMap<>();
        result.put("foo", "bar");
        result.put("counter", 23);
        return result;
    };
}
```

# Metrics

The actuator provides a metrics service with _gauge_ and _counter_ support. A _gauge_
records a single value (such as the execution time of an operation); and a _counter_
records a delta (an increment or decrement)

Looking at <http://localhost:8080/metrics>, we can see the authentication attempt
and failure on endpoints.

# CRaSH

The dashboard feature of CRaSH is awesome and it is really easy to integrate with
Boot. It is also a nice way to show how roles management can be configured easily

Let's first add another dependency to our project

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-remote-shell</artifactId>
</dependency>
```

And just start our app. This will initialize CRaSH with SSH server on port 2000. Because
we have already integrated Spring Security, CRaSH will nicely integrate with it and
use our custom role (as specified by `management.security.role`) instead of the default
`ADMIN` role.

So all that's left is to SSH our instance: `ssh hero@localhost -p 2000`

# JMX

If time permits, open `JConsole` and show that these endpoints are available through
JMX as well.

