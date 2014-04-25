## Spring 4.1 annotated endpoints

This simple project demonstrates the JMS listener endpoint feature to be introduced
in Spring framework 4.1.


### Building

To build this project, you first need the latest developments that are available on
master, checkout [the code](https://github.com/spring-project/spring-framework/) if
you haven't already.

Simple run `./gradlew install` to update your local maven repository with Spring
version `4.1.0-BUILD-SNAPSHOT`

### Running

To run the project, either invoke `mvn spring-boot:run` or run the `Application` main
method from your IDE.

This demo app uses an embedded HornetQ broker.