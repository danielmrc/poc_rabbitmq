package br.com.pocrabbitmq.broker;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import org.apache.log4j.Logger;


//import io.smallrye.reactive.messaging.rabbitmq.RabbitMQMessageSender;

public class Producer {
	
	//private RabbitMQMessageSender sender;
	
	private static final Logger log = Logger.getLogger("br.com.pocrabbitmq.broker.Producer");
	
	private static final String MESSAGETEST = "Testando envio de mensagens com prioridade rabbitmq";
	private static final String EXCHANGENAME = "ne";
	private static final String EXCHANGETYPE = "direct";
	private static final String QUEUENAME = "smp01";
	
	private Channel channel;
	
	Broker broker;
	
	public Producer() throws IOException{
		broker = new Broker();
		channel = broker.getChannel();
		log.info("Consumidor inicializado!");
	}
	public void sendMessage() {
		
		byte[] msg = MESSAGETEST.getBytes();
		
		try {
			channel.exchangeDeclare(EXCHANGENAME, EXCHANGETYPE, true);
			channel.queueDeclare(QUEUENAME, true, false, false, null);
			channel.queueBind(QUEUENAME, EXCHANGENAME, QUEUENAME);
			
			channel.basicPublish(EXCHANGENAME, QUEUENAME, getProperties(), msg);
			log.info("Mensagem enviada com sucesso!");
		}catch(IOException e) {
			log.error("Erro ao tentar enviar mensagem para a fila [" + QUEUENAME + "]" + e.getMessage());
		}
			
		
	}
	
	private BasicProperties getProperties() {
		BasicProperties prop;
		
		prop = new AMQP.BasicProperties.Builder()
				.contentType("text/plain")
				.deliveryMode(2)
				.priority(1)
				.build();
				
		return prop;
	}

}
