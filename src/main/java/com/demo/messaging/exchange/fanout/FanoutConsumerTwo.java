package com.demo.messaging.exchange.fanout;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class FanoutConsumerTwo extends FanoutEndpoint  {

	public FanoutConsumerTwo() throws IOException {
		super();
	}

	public static void main(String[] args) throws IOException {
		FanoutConsumerOne one = new FanoutConsumerOne();
		one.createQueue("fanoutDemoQueueTwo");

		Consumer consumer = new DefaultConsumer(one.getFanoutExchangeChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Consumer Two [x] Received '" + message
						+ "'");
			}
		};
		one.getFanoutExchangeChannel().basicConsume("fanoutDemoQueueTwo", true, consumer);
	}
}
