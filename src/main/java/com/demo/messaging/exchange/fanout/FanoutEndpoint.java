package com.demo.messaging.exchange.fanout;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class FanoutEndpoint {

	public static final String EXCHANGE_TYPE_FANOUT = "fanout";
	public static final String EXCHANGE_NAME = "fanout-exchange-demo";
	
	protected Channel fanoutExchangeChannel;
	
	public FanoutEndpoint() throws IOException {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		Connection connection = factory.newConnection();
		
		fanoutExchangeChannel = connection.createChannel();
		fanoutExchangeChannel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE_FANOUT);
	}

	public void createQueue(String queueName) throws IOException {
		fanoutExchangeChannel.queueDeclare(queueName, false, false, false, null);
		fanoutExchangeChannel.queueBind(queueName, EXCHANGE_NAME, "");
	}
	
	public Channel getFanoutExchangeChannel() {
		return fanoutExchangeChannel;
	}
	
}
