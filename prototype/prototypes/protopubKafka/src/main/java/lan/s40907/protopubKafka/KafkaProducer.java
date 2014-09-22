package lan.s40907.protopubKafka;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class KafkaProducer {	
	
	public Producer<Integer, byte[]> GetProducer() {
		Properties properties = new Properties();		
		/*
		properties.put("metadata.broker.list", "192.168.1.185:9092");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("request.required.acks", "-1");*/
		//properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("request.required.acks", "1");
		properties.put("metadata.broker.list", "localhost:9092");
		properties.put("producer.type", "async");
		properties.put("batch.size", 1);		
		return new Producer<Integer, byte[]>(new ProducerConfig(properties));
	}
}
