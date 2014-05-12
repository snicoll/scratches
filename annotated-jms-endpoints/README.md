## Spring 4.1 annotated endpoints

This simple project demonstrates the JMS listener endpoint feature to be introduced
in Spring framework 4.1.


### Building

You should get the latest development from the [snapshot repository](http://repo.spring.io/snapshot). If
that does not work out for you, you can build the framework yourself: checkout
[the code](https://github.com/spring-project/spring-framework/) if you haven't already.

Simple run `./gradlew install` to update your local maven repository with Spring
version `4.1.0-BUILD-SNAPSHOT`

### Running

To run the project, either invoke `mvn spring-boot:run` or run the `Application` main
method from your IDE.

This demo app uses an embedded HornetQ broker.