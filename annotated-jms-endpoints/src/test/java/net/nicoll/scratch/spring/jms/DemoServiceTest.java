package net.nicoll.scratch.spring.jms;


import static org.junit.Assert.*;

import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import net.nicoll.scratch.spring.jms.support.BootstrapHornetQConfig;
import org.hornetq.jms.client.HornetQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Stephane Nicoll
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DemoServiceTest {

	private final Logger logger = LoggerFactory.getLogger(DemoServiceTest.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private DemoService demoService;

	@Test
	public void sample() throws JMSException {
		demoService.clearLogs();
		sendMessage("testQueue", "Hello World");

		// This should hit the first queue and send the response
		sleep(1000);
		List<String> logs = demoService.getLogs();
		assertEquals("Reply was not received", 1, logs.size());
		assertEquals("Wrong reply message", "Hello World", logs.get(0));
	}

	private void sendMessage(String destination, final String content) {
		logger.info("Sending a simple message to [" + destination + "]");
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(content);
			}
		});
	}

	@Configuration
	@Import(BootstrapHornetQConfig.class)
	@EnableJms
	static class Config implements JmsListenerConfigurer {

		@Autowired
		private BootstrapHornetQConfig hornetQConfig;

		@Override
		public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
			registrar.setDefaultContainerFactory(defaultContainerFactory());
		}

		@Bean
		public DemoService echoService() {
			return new DemoService();
		}

		@Bean
		public JmsListenerContainerFactory defaultContainerFactory() {
			DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
			factory.setConnectionFactory(hornetQConfig.connectionFactory());
			factory.setDestinationResolver(hornetQConfig.destinationResolver());
			factory.setConcurrency("4-5");
			return factory;
		}

		@Bean
		public Queue testQueue() {
			return new HornetQQueue("testQueue");
		}

		@Bean
		public Queue anotherQueue() {
			return new HornetQQueue("anotherQueue");
		}

		@Bean
		JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
			return new JmsTemplate(connectionFactory);
		}

	}

	private void sleep(long msec) {
		try {
			Thread.sleep(msec);
		}
		catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
