package lan.s40907.protopubKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaConsumer {
	private ConsumerConnector consumerConnector = null;
	
	public ConsumerConnector GetConsumer() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "localhost:2181");
		properties.put("group.id", "testGroup");
		//properties.put("zookeeper.session.timeout.ms", "400");
		//properties.put("zookeeper.sync.time.ms", "250");
		//properties.put("auto.commit.interval.ms", "100");
		//properties.put("fetch.size", 1024*1024);
		//properties.put("batch.size", 1);
		properties.put("fetch.size", 1024*1024);
		properties.put("num.consumer.fetchers", "2");
		
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
		return consumerConnector;
		
		
	}
	
	public KafkaStream<byte[],byte[]> GetFirstKafkaStream(HashMap<String, 
			Integer> topics, String topicName) {
		
		Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = 
				GetConsumer().createMessageStreams(topics);		
		return messageStreams.get(topicName).get(0);
	}
	
	public void Close() {
		if (consumerConnector != null) {
			consumerConnector.shutdown();
		}
	}
}
