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
		log.info("Initializing consumer!");
	}
	public void sendMessage() {
		
		//byte[] msg = MESSAGETEST.getBytes();
		
		try {
			channel.exchangeDeclare(EXCHANGENAME, EXCHANGETYPE, true);
			channel.queueDeclare(QUEUENAME, true, false, false, null);
			channel.queueBind(QUEUENAME, EXCHANGENAME, QUEUENAME);
			
			generateMessages();
			log.info("Mensagem enviada com sucesso!");
		}catch(IOException e) {
			log.error("Error trying send message to the queue = [" + QUEUENAME + "]" + e.getMessage());
		}
			
		
	}
	
	
	private void generateMessages() {
		StringBuilder msg = new StringBuilder();
		msg.append("message with priority =");
		
		for(int i = 0; i < 150; i++) {
			msg.append(i % 10);
			String msgStr = msg.toString();
			try {
				channel.basicPublish(EXCHANGENAME, QUEUENAME, getProperties(i%10), msgStr.getBytes());
				log.info("Message send to QUEUE = [" + QUEUENAME + " msg = [" + msgStr);
			}catch(IOException e) {
				log.info("Error while trying send messages " + e.getMessage());
			}
		}
	}
	
	
	private BasicProperties getProperties(int priority) {
		BasicProperties prop;
		
		prop = new AMQP.BasicProperties.Builder()
				.contentType("text/plain")
				.deliveryMode(2)
				.priority(1)
				.build();
				
		return prop;
	}

}
