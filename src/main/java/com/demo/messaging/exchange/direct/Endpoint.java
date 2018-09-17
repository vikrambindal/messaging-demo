package com.demo.messaging.exchange.direct;

import java.io.IOException;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class Endpoint {

	protected String endPointName;
	private Connection connection;
	protected Channel channel;

	public Endpoint(String endpointName) throws IOException {
		this.endPointName = endpointName;

		// Create a connection factory
		ConnectionFactory factory = new ConnectionFactory();

		// hostname of your rabbitmq server
		//factory.setHost("p16-d-rabmq.us16.kexpress.net");
		factory.setUsername("radmin");
		factory.setPassword("4abb1tMq@dm1n");

		Address[] addr = Address.parseAddresses("p16-d-rabmq-001.us16.kexpress.net,p16-d-rabmq-002.us16.kexpress.net,"
				+ "p16-d-rabmq-003.us16.kexpress.net" );
		// getting a connection
		connection = factory.newConnection(addr);

		// creating a channel
		channel = connection.createChannel();
		channel.confirmSelect();

		// declaring a queue for this channel. If queue does not exist,
		// it will be created on the server.
		channel.queueDeclare(endpointName, true, false, false, null);
		channel.addShutdownListener(new ShutdownListener() {
			
			public void shutdownCompleted(ShutdownSignalException cause) {
				System.out.println("Shutting down node " + cause.getMessage());
			}
		});
	}

	public void close() throws IOException {
		this.channel.close();
		this.connection.close();
	}
}
