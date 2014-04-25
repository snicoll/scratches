package net.nicoll.scratch.spring.jms;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.JmsMessageHeaderAccessor;
import org.springframework.jms.support.converter.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Stephane Nicoll
 */
@Component
public class DemoService {

	private final Logger logger = LoggerFactory.getLogger(DemoService.class);

	private final List<String> logs = new ArrayList<String>();


	@JmsListener(destination = "testQueue")
    @SendTo("anotherQueue")
	public Message<String> echo(String input, JmsMessageHeaderAccessor headerAccessor) {
		logger.info("Sending back: " + input + " (messageId=" + headerAccessor.getMessageId() + ")");
		return MessageBuilder.withPayload(input)
				.setHeader("myCustomHeader", "foo")
				.setHeader(JmsHeaders.TYPE, "myJmsType")
				.build();
	}

	@JmsListener(destination = "anotherQueue")
	public void log(String input, @Header(JmsHeaders.CORRELATION_ID) String correlationId) {
		logger.info("Received: " + input + " (correlationId=" + correlationId + ")");
		logs.add(input);
	}

	void clearLogs() {
		logs.clear();
	}

	List<String> getLogs() {
		return logs;
	}
}
