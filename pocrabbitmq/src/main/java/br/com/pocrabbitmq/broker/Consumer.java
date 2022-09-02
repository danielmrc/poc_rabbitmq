package br.com.pocrabbitmq.broker;

import java.io.IOException;
import java.util.Arrays;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;

import org.apache.log4j.Logger;


public class Consumer {
	
	private static final Logger log = Logger.getLogger("br.com.pocrabbitmq.broker.Consumer");
	
	private static final String QUEUENAME = "smp01";
	
	private static boolean autoAck = false;
	
	Channel channel;
	
	Broker broker;
	
	public Consumer() throws IOException{
		broker = new Broker();
		channel = broker.getChannel();
		log.info("Initialing Consumidor!");
	}
	
	public void onMessage() {
		try {
			GetResponse response = channel.basicGet(QUEUENAME, autoAck);
			
			if(response != null) {
				byte[] msg = response.getBody();
				String finalMsg = Arrays.toString(msg);
				AMQP.BasicProperties props = response.getProps();
				log.info("Message read in queue = [" + QUEUENAME + "] with priority = [" +props.getPriority()+ "] :" + finalMsg);
				sendAck(response.getEnvelope().getDeliveryTag());
			}else {
				log.warn("Void response!");
			}
		}catch(IOException e) {
			log.error("Error trying consume message");
		}
	}
	
	
	private void sendAck(Long deliveryTag) {
		try {
			channel.basicAck(deliveryTag, autoAck);
			log.info("ACK sending with sucess for message with tag = [" + deliveryTag + "]");
		}catch(IOException e) {
			log.error("Error trying sending ACK for message tag = [" + deliveryTag + "]");
		}
	}
}
