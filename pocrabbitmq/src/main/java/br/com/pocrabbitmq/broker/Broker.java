package br.com.pocrabbitmq.broker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Broker {
	
	private static final Logger log = Logger.getLogger("br.com.pocrabbitmq.broker.Broker");
	
	private static final String HOST = "localhost";
	private static final int PORT = 5672;
	private static final String USERNAME = "sisv3";
	private static final String PASS = "s1sv3";
	
	private ConnectionFactory factory;
	
	private Connection conn;
	
	private Channel channel;
	
	public Broker() {
		this.factory = new ConnectionFactory();
		log.info("Initializing Broker!");
	}

	
	private Connection getConnect() {
		factory.setUsername(USERNAME);
		factory.setPassword(PASS);
		factory.setHost(HOST);
		factory.setPort(PORT);
		try {
			conn = factory.newConnection("conexao-geral");
			log.info("Connection build!");
		}catch(TimeoutException e) {
			log.error("Timeout trying build connection!" + e.getMessage());
		}catch(IOException e) {
			log.error("Error trying create connection! " + e.getMessage());
		}
		return conn;
	}
	
	public Channel getChannel(){
		try {
			channel = getConnect().createChannel();
			log.info("Channel create!");
		}catch(IOException e) {
			log.error("Error trying create channel! " + e.getMessage());
		}
		
		return channel;
	}
	
}
