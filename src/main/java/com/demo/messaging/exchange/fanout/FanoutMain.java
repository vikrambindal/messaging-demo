package com.demo.messaging.exchange.fanout;

import java.io.IOException;

public class FanoutMain {

	public static void main(String[] args) throws IOException {
		
		FanoutPublisher publisher = new FanoutPublisher();
		
		publisher.publishMessage("Hello Fanout");
	}
}
