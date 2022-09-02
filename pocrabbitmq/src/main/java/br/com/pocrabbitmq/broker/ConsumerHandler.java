package br.com.pocrabbitmq.broker;

import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class ConsumerHandler {
	
	private final Logger log = Logger.getLogger("br.com.pocrabbitmq.broker.ConsumerHandler");
	
	private static String consumerTag;
	private static final String QUEUENAME = "smp01";
	
	private Broker broker;
	
	private boolean autoAck = false;
	
	private Channel channel;
	
	public ConsumerHandler(String tag) {
		broker = new Broker();
		channel = broker.getChannel();
		consumerTag = tag;
		log.info("ConsumerHandler inicializado");
	}
	
	public void initConsumer() {
		
		try {
			channel.basicConsume(QUEUENAME, autoAck, consumerTag, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag,
						Envelope envelope,
						AMQP.BasicProperties properties,
						byte[] body) throws IOException{
					String finalMsg = Arrays.toString(body);
					log.info("Message read in queue = [" + QUEUENAME + "] with priority = [" + properties.getPriority() + "] :" + finalMsg);
					sendAck(envelope.getDeliveryTag());					
				}
			});
		}catch(IOException e) {
			log.info("Error found trying initializing consumer" + e.getMessage());
		}
	}
	
	private void sendAck(Long deliveryTag) {
		try {
			channel.basicAck(deliveryTag, autoAck);
			log.info("ACK sending sucess to message with tag  = [" + deliveryTag + "]");
		}catch(IOException e) {
			log.error("Error trying send ACK for message tag = [" + deliveryTag + "]");
		}
	}

}
