package com.pluralsight.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.enterprise.inject.Produces;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ClientProducer {
	
	@Produces
	public Client produceClient() {
		
		final Properties p = new Properties();

		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		InputStream input = null;

		Client client = null;
		try {
			input = loader.getResourceAsStream("config.properties");

			p.load(input);

			final String host = p.getProperty("elasticSearchHost").trim();

			final String port = p.getProperty("elasticSearchPort").trim();

			final int portNumber = Integer.parseInt(port);

			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), portNumber));

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}
}

