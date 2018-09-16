package com.demo.messaging.exchange.fanout;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class FanoutConsumerOne extends FanoutEndpoint {

	public FanoutConsumerOne() throws IOException {
		super();
	}

	public static void main(String[] args) throws IOException {
		FanoutConsumerOne one = new FanoutConsumerOne();

		one.createQueue("fanoutDemoQueueOne");
		Consumer consumer = new DefaultConsumer(one.getFanoutExchangeChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("Consumer One [x] Received '" + message
						+ "'");
			}
		};
		one.getFanoutExchangeChannel().basicConsume("fanoutDemoQueueOne", true, consumer);
	}
}
