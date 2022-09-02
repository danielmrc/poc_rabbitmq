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
		log.info("Consumidor inicializado!");
	}
	
	public void onMessage() {
		try {
			GetResponse response = channel.basicGet(QUEUENAME, autoAck);
			
			if(response != null) {
				byte[] msg = response.getBody();
				String finalMsg = Arrays.toString(msg);
				AMQP.BasicProperties props = response.getProps();
				log.info("Mensagem lida da fila [" + QUEUENAME + "] com prioridade [" +props.getPriority()+ "] :" + finalMsg);
				sendAck(response.getEnvelope().getDeliveryTag());
			}else {
				log.warn("Resposta vazia!");
			}
		}catch(IOException e) {
			log.error("Erro ao tentar consumir mensagem");
		}
	}
	
	
	private void sendAck(Long deliveryTag) {
		try {
			channel.basicAck(deliveryTag, autoAck);
			log.info("ACK enviado com sucesso para a mensagem de tag = [" + deliveryTag + "]");
		}catch(IOException e) {
			log.error("Erro ao tentar enviar ACK para a mensagem de tag = [" + deliveryTag + "]");
		}
	}
}
