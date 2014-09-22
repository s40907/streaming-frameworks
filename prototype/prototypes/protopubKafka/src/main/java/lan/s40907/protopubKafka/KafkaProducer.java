package lan.s40907.protopubKafka;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class KafkaProducer {	
	
	public Producer<Integer, byte[]> GetProducer() {
		Properties properties = new Properties();
		properties.put("request.required.acks", "1");
		properties.put("metadata.broker.list", "localhost:9092");
		properties.put("producer.type", "async");
		return new Producer<Integer, byte[]>(new ProducerConfig(properties));
	}
}
