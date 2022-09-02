package br.com.pocrabbitmq;

import br.com.pocrabbitmq.broker.Producer;
import br.com.pocrabbitmq.broker.Consumer;
import java.lang.InterruptedException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main {
	
	static Logger log = Logger.getLogger("br.com.pocrabbitmq.Main");
	
	static Consumer consumer;
	
	static Producer producer;
	
    public static void main(String... args) {
    	BasicConfigurator.configure();
    	try {
	    	producer = new Producer();
	        producer.sendMessage();
	        
	        log.info("Em espera...");
	        Thread.sleep(40000);
	        
	        consumer = new Consumer();
	        consumer.onMessage();

    	}catch(IOException e) {
    		log.error("Erro ao tentar produzir mensagem!" + e.getMessage());
    	}catch(InterruptedException e) {
        	log.error("Erro no sleep da thread " + e.getMessage());
        }
    }
}