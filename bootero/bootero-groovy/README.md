This demo is a gentle introduction to what Spring Boot can do with a very
simple rest controller.

# Boot cli

To demonstrate boot-cli, let's simply run `spring run hero.groovy` to access our
service at <http://localhost:8080>

The cli [can be installed easily][cli-install] either using `brew` or `gvm`.

# Executable jar

Let's transform our simple script in a full-featured executable jar. This shows
how we can easily build a standalone application that can run virtually anywhere

`spring jar hero.jar hero.groovy`

Once the jar has been built, the application can be stated as follows:

`java -jar hero.jar`

Boot has a very powerful configuration mechanism. For instance, the following
command changes the HTTP port to 7070:

`java -jar hero.jar  --server.port=7070`


[cli-install]: http://docs.spring.io/spring-boot/docs/1.0.2.RELEASE/reference/htmlsingle/#getting-started-installing-the-cli