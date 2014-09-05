package org.test

@EnableRabbit
@Log
class JmsSample {

	@RabbitListener(queues = 's2gx2014')
	def processMsg(String msg) {
		log.info("Received $msg")
	}

	@Bean Queue s2gx2014Queue() {
		new Queue('s2gx2014', false)
	}
}