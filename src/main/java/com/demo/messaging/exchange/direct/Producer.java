package com.demo.messaging.exchange.direct;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

public class Producer extends Endpoint {

	public Producer(String endpointName) throws IOException {
		super(endpointName);
	}

	public void sendMessage(Serializable object) throws IOException {
		channel.basicPublish(""	, endPointName, null, SerializationUtils.serialize(object));
	}
}
