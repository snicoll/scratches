package io.spring.initializr;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Stephane Nicoll
 */
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties(ProjectMetadata.class)
public class TestApp {

}
