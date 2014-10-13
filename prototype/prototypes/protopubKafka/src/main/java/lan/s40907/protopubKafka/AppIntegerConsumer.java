package lan.s40907.protopubKafka;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import lan.s40907.protopubKafka.helper.Convert;
import lan.s40907.protopubKafka.helper.Transpose;

import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;
import com.yammer.metrics.core.Clock;

public class AppIntegerConsumer extends Thread {
	private static Logger logger = Logger.getLogger(AppIntegerConsumer.class);
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();
	private Convert convert = new Convert();
	private Transpose transpose = new Transpose();
	private long currentTimeSeconds = Clock.defaultClock().time();
	private int normalizedSecond = 0;
	private int countPerSecond = 0;
	private String topicName;
	private KafkaConsumer kafkaConsumer;
	
	public AppIntegerConsumer(String topicName) {
		this.topicName = topicName;
		kafkaConsumer = new KafkaConsumer();
	}

	@Override
	public void run() {
		HashMap<String,Integer> topics = new HashMap<String, Integer>();
		topics.put(topicName, 1);		
		
		KafkaStream<byte[],byte[]> kafkaStream = kafkaConsumer.GetFirstKafkaStream(topics, topicName);				
		ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
		logger.info("Iterating");
		while (iterator.hasNext()) {
			MessageAndMetadata<byte[],byte[]> data = iterator.next();
			Map<String, byte[]> hashMap = null;
			try {
				hashMap = convert.toMapFrom(data.message());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}			
			String value = new String(hashMap.get("word"));			
			if (counter.containsKey(value)) {
				Integer amount = counter.get(value);
				counter.put(value, ++amount);
			} else {
				counter.put(value, 1);
			}
			
			printMessagesPerSecond();
			countPerSecond++;
		}
		logger.info("Closing");
		kafkaConsumer.Close();
	}


	private void printMessagesPerSecond() {
		if ( (currentTimeSeconds + 1000) <  Clock.defaultClock().time() ) {
			String takeFirstHeighest = transpose.takeFirstHighest(1, counter);			
			OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean)
					ManagementFactory.getOperatingSystemMXBean();
			logger.info(String.format("%s, %s, %s, %s", normalizedSecond, countPerSecond, operatingSystem.getSystemCpuLoad(), takeFirstHeighest ));
			normalizedSecond = normalizedSecond + 1;
			countPerSecond = 1;
			currentTimeSeconds = Clock.defaultClock().time();
		}
	};
		
	public static void main(String[] args) {
		String topicName = "integerTopic";
		if (args.length > 0) {
			topicName = args[0];
		}
		
		AppIntegerConsumer kafkaConsumer = new AppIntegerConsumer(topicName);
		kafkaConsumer.start();		
	}
}
