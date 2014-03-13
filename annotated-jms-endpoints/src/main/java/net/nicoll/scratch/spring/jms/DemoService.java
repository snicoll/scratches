package net.nicoll.scratch.spring.jms;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jms.annotation.JmsListener;

/**
 *
 * @author Stephane Nicoll
 */
public class DemoService {

	private final Logger logger = LoggerFactory.getLogger(DemoService.class);

	private final List<String> logs = new ArrayList<String>();


	@JmsListener(destination = "testQueue", responseDestination = "anotherQueue")
	public String echo(String input) {
		logger.info("Sending back: " + input);
		return input;
	}

	@JmsListener(destination = "anotherQueue")
	public void log(String input) {
		logger.info("received: " + input);
		logs.add(input);
	}

	void clearLogs() {
		logs.clear();
	}

	List<String> getLogs() {
		return logs;
	}
}
