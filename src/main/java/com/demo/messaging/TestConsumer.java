package com.demo.messaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;

import com.demo.messaging.exchange.direct.Endpoint;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class TestConsumer extends Endpoint {

	public TestConsumer(String endpointName) throws IOException {
		super(endpointName);
		
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope env,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				Map map = (HashMap)SerializationUtils.deserialize(body);
				System.out.println(String.format("Consumer [redeliver=%s consumerTag=%s ] MsgNumber Rx: %s", 
			    		new Object[] {env.isRedeliver(), consumerTag, map.get("message number")}));
			}
		};
		channel.basicConsume("project-queue", true, consumer);
	}
	
	public static void main(String[] args) throws IOException {
		new TestConsumer("project-queue");
	}

}
