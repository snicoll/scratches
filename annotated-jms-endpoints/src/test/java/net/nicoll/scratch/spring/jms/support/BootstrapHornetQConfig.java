package net.nicoll.scratch.spring.jms.support;

import java.util.Collection;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.integration.spring.SpringJmsBootstrap;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.JMSQueueConfiguration;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;

import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.jms.support.destination.BeanFactoryDestinationResolver;
import org.springframework.jms.support.destination.DestinationResolver;

/**
 * A simple hornetQ bootstrap configuration, exposing a {@link ConnectionFactory}
 * and a {@link DestinationResolver} suitable an embedded broker.
 * <p>Register all {@link Queue} found in the context.
 *
 * @author Stephane Nicoll
 */
@Configuration
public class BootstrapHornetQConfig {

	@Bean(initMethod = "start", destroyMethod = "stop")
	public SpringJmsBootstrap embeddedServer(Collection<Queue> queues) {
		SpringJmsBootstrap bootstrap = new SpringJmsBootstrap();
		bootstrap.setConfiguration(hornetQConfiguration());
		bootstrap.setJmsConfiguration(jmsConfiguration(queues));
		return bootstrap;
	}

	@Bean
	public ConfigurationImpl hornetQConfiguration() {
		ConfigurationImpl configuration = new ConfigurationImpl();

		configuration.setJournalDirectory("target/data/journal");
		configuration.setPersistenceEnabled(false);
		configuration.setSecurityEnabled(false);
		configuration.getAcceptorConfigurations().add(
				new TransportConfiguration(InVMAcceptorFactory.class.getName()));
		return configuration;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		ServerLocator serverLocator = HornetQClient.createServerLocatorWithoutHA(
				new TransportConfiguration(InVMConnectorFactory.class.getName()));
		return new HornetQConnectionFactory(serverLocator);
	}

	@Bean
	public DestinationResolver destinationResolver() {
		return new BeanFactoryDestinationResolver();
	}

	@Bean // faster shutdown
	public LifecycleProcessor defaultLifecycleProcessor() {
		DefaultLifecycleProcessor lifecycleProcessor = new DefaultLifecycleProcessor();
		lifecycleProcessor.setTimeoutPerShutdownPhase(0);
		return lifecycleProcessor;
	}

	private JMSConfiguration jmsConfiguration(Collection<Queue> queues) {
		JMSConfiguration jmsConfig = new JMSConfigurationImpl();

		for (Queue queue : queues) {
			JMSQueueConfiguration queueConfig = createQueueConfiguration(queue);
			jmsConfig.getQueueConfigurations().add(queueConfig);
		}

		return jmsConfig;
	}

	private JMSQueueConfiguration createQueueConfiguration(Queue queue) {
		try {
			String name = queue.getQueueName();
			return new JMSQueueConfigurationImpl(name, null, false, "/queue/" + name);
		}
		catch (JMSException e) {
			throw new IllegalStateException("Could not get the name of " + queue, e);
		}
	}
}
