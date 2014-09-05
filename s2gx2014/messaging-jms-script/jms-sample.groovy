package org.test

@Grab('spring-boot-starter-hornetq')

@EnableJms
@Log
class JmsSample {

	@JmsListener(destination = 's2gx2014')
	def processMsg(String msg) {
		log.info("Received $msg")
	}
}
