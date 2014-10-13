package lan.s40907.protopubKafka;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import lan.s40907.protopubKafka.helper.Convert;

import org.apache.log4j.Logger;

public class AppIntegerProducer {
	private static Logger logger = Logger.getLogger(AppIntegerProducer.class);
	private Convert convert = new Convert();
	private String topicName;
	private long amountOfNumbers;	
	
	public AppIntegerProducer(String topicName, long amountOfNumbers) {		
		this.topicName = topicName;
		this.amountOfNumbers = amountOfNumbers;
	}	
	
	public void start() throws IOException {
		KafkaProducer kafkaProducer = new KafkaProducer();		
		Producer<Integer, byte[]> producer = kafkaProducer.GetProducer();
		Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
		byte[] staticPayload = new byte[100]; 
		Arrays.fill(staticPayload, (byte)0);
		hashMap.put("word", staticPayload);
		for (long i = 0; i < amountOfNumbers; i++) {			
			KeyedMessage<Integer, byte[]> keyedMessage = 
			  new KeyedMessage<Integer, byte[]>(topicName, convert.toByteFrom(hashMap));
			producer.send(keyedMessage);
		}
		producer.close();
	}

	public static void main(String[] args) throws IOException {
		String topicName = "integerTopic";
		if (args.length > 0) {
			topicName = args[0];
		}	
		
		logger.info("Starting producer");	
		AppIntegerProducer kafkaProducer = new AppIntegerProducer(topicName, 20000000);
		kafkaProducer.start();
		logger.info("Producer stopped");
	}
}