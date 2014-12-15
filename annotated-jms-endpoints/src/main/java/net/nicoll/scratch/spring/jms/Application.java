package net.nicoll.scratch.spring.jms;

import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 *
 * @author Stephane Nicoll
 */
@SpringBootApplication
@EnableJms // Technically not required, Spring Boot app auto-configure it.
public class Application implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private DemoService demoService;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Bean
	public JmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setConcurrency("4-5");
		return factory;
	}

	@Override
	public void run(String... strings) throws Exception {
		// This should hit the first queue and send the response
		sendMessage("testQueue", "Hello World");
		sleep(1000);

		// Checking the result
		List<String> logs = this.demoService.getLogs();
		if (logs.isEmpty()) {
			throw new IllegalStateException("No reply received.");
		}
		String replyMessage = logs.get(0);
		if (!replyMessage.equals("Hello World")) {
			throw new IllegalStateException("Wrong reply message '" + replyMessage + "'");
		}

		this.applicationContext.close(); // bye bye
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}


	private void sendMessage(String destination, final String content) {
		logger.info("Sending a simple message to [" + destination + "]");
		this.jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(content);
			}
		});
	}

	private static void sleep(long msec) {
		try {
			Thread.sleep(msec);
		}
		catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
