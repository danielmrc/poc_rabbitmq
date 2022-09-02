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
		log.info("Broker inicializado!");
	}

	
	private Connection getConnect() {
		factory.setUsername(USERNAME);
		factory.setPassword(PASS);
		factory.setHost(HOST);
		factory.setPort(PORT);
		try {
			conn = factory.newConnection("conexao-geral");
			log.info("Conexão criada!");
		}catch(TimeoutException e) {
			log.error("Timeout ao tentar criar conexão!" + e.getMessage());
		}catch(IOException e) {
			log.error("Erro ao tentar criar conexao! " + e.getMessage());
		}
		return conn;
	}
	
	public Channel getChannel(){
		try {
			channel = getConnect().createChannel();
			log.info("Canal Criado!");
		}catch(IOException e) {
			log.error("Erro ao tentar criar canal! " + e.getMessage());
		}
		
		return channel;
	}
	
}
