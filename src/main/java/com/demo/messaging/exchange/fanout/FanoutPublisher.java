package com.demo.messaging.exchange.fanout;

import java.io.IOException;

public class FanoutPublisher extends FanoutEndpoint {

	public FanoutPublisher() throws IOException {
		super();
	}

	public void publishMessage(String msg) throws IOException {
		
		fanoutExchangeChannel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
	}
}
