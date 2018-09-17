package com.demo.messaging;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.demo.messaging.exchange.direct.Endpoint;
import com.demo.messaging.exchange.direct.Producer;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Hello world!
 *
 */
public class TestPublisher
{
	int numLoops = 3;
	int messagesPerLoop = 10;
	int loopDelay = 300;
	int messageDelay = 100;
	private Connection connection;
	protected Channel channel;
	
    public TestPublisher() throws IOException {
	}

	public static void main( String[] args ) throws IOException, InterruptedException, TimeoutException
    {
		TestPublisher producer = new TestPublisher();
		producer.init();
		producer.run();
		
    }
	
	private void init() throws IOException {
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
			channel.queueDeclare("project-queue", true, false, false, null);
//			channel.addShutdownListener(new ShutdownListener() {
//				
//				public void shutdownCompleted(ShutdownSignalException cause) {
//					throw new ShutdownSignalException(false, false, cause, cause);
//					//System.out.println("Shutting down node " + cause.getMessage());
//				}
//			});
	}
    
    private void send(Serializable object) throws IOException, InterruptedException, TimeoutException
	{
		System.out.println(String.format("Sending message: %s", SerializationUtils.serialize(object)));
		channel.basicPublish(""	, "project-queue", null, SerializationUtils.serialize(object));
		//channel.basicPublish("", queue, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
		channel.waitForConfirmsOrDie(500);
	}
    
    private void run() throws IOException, InterruptedException, TimeoutException
	{
		for (int loopNum=1; loopNum <= numLoops; loopNum++)
		{
			for (int messageNum=1; messageNum <= messagesPerLoop; messageNum++)
			{
				String message = String.format("Message %d.%d", loopNum, messageNum);
				try
				{
					send(message);
				}
				catch (AlreadyClosedException ex)
				{
					System.out.println("Connection error detected, re-initialising connection. \n" + ex.getMessage());
					init();
					send(message);
				}
				catch (ShutdownSignalException ex)
				{
					System.out.println("Shutdown signal detected, re-initialising connection. \n" + ex.getMessage());
					init();
					send(message);
				}			
				catch (TimeoutException ex)
				{
					System.out.println("Timeout detected, re-initialising connection. \n" + ex.getMessage());
					init();
					send(message);
					
				}
				catch (IOException ex)
				{
					System.out.println("IOException detected, re-initialising connection. \n" + ex.getMessage());
					init();
					send(message);
				}
				catch (Exception ex)
				{
					System.out.println("IOException detected, re-initialising connection. \n" + ex.getMessage());
					init();
					send(message);
				}
				//Thread.sleep(messageDelay);
			}
			
			// delay between loops
			if (loopNum < numLoops)
				Thread.sleep(loopDelay);
		}
		
		channel.close();
		connection.close();
	}
}
