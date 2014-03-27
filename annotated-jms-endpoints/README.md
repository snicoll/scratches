## Spring 4.1 annotated endpoints

This simple project demonstrates the JMS listener endpoint feature to be introduced
in Spring framework 4.1.


### Building

To build this project, you first need the latest developments that are available on
my fork, checkout the `SPR-9882` branch of [my fork](https://github.com/snicoll/spring-framework/)

Simple run `./gradlew install` to update your local maven repository with Spring
version `4.1.0-BUILD-SNAPSHOT`

Open the project in your IDE and run the `DemoServiceTest`. It uses an embedded HornetQ server.