package br.com.pocrabbitmq;

import br.com.pocrabbitmq.broker.Producer;
import br.com.pocrabbitmq.broker.Consumer;
import br.com.pocrabbitmq.broker.ConsumerHandler;
import java.lang.InterruptedException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {
	
	static Logger log = Logger.getLogger("br.com.pocrabbitmq.Main");
	
	static Consumer consumer;
	
	static Producer producer;
	
	static ConsumerHandler consumerHandler;
	
    public static void main(String... args) {
    	BasicConfigurator.configure();
    	try {
	    	producer = new Producer();
	        producer.sendMessage();
	        
	        log.info("Waiting...");
	        Thread.sleep(4000);
	        /*
	        consumer = new Consumer();
	        consumer.onMessage();
	    	*/
    		
    		consumerHandler = new ConsumerHandler("smp01");
    		consumerHandler.initConsumer();
    		
    		Thread.sleep(4000);
    		log.info("Waiting...");
    		
    		producer.sendMessage();
    		
    	}catch(IOException e) {
    		log.error("Error" + e.getMessage());
    	}catch(InterruptedException e) {
        	log.error("Error" + e.getMessage());
        }
    }
}