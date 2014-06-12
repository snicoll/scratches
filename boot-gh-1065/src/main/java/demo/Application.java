package demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Configuration
	static class DataInitializer implements CommandLineRunner {

		@Autowired
		private FooRepository repository;

		@Override
		public void run(String... args) throws Exception {
			repository.save(Arrays.asList(
					new Foo("John", "Doe"),
					new Foo("Joe", "Smith")
			));
		}
	}
}
