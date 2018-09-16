package com.demo.messaging;

import java.io.IOException;
import java.util.HashMap;

import com.demo.messaging.exchange.direct.Producer;
import com.demo.messaging.exchange.direct.QueueConsumer;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws IOException
    {
        System.out.println( "Hello World!" );
        QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();
		
		Producer producer = new Producer("queue");
		
		/*for (int i = 0; i < 10; i++) {
			HashMap message = new HashMap();
			message.put("message number", i);
			producer.sendMessage(message);
			System.out.println("Message Number "+ i +" sent.");
		}*/
		
		/*while (true) {
		}*/
    }
}
