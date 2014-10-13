package lan.s40907.protopubKafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import lan.s40907.protopubKafka.helper.Convert;
import lan.s40907.protopubKafka.helper.Transpose;
import org.apache.log4j.Logger;

public class AppWordCountProducer {
	private static Logger logger = Logger.getLogger(AppWordCountProducer.class);
	private Convert convert = new Convert();
	private Transpose transpose = new Transpose();
	private String topicName;
	private String fileToRead = "shaks12.txt";
	private int retries;
	
	public AppWordCountProducer(String topicName, int retries) {		
		this.topicName = topicName;
		this.retries = retries;
	}
	
	public void start() throws IOException {
		logger.info("Reading text");
		List<String> lines = transpose.readAllLines("/" + fileToRead);
		logger.info("found " + lines.size() + " line(s)");		
		KafkaProducer kafkaProducer = new KafkaProducer();
		Producer<Integer, byte[]> producer = kafkaProducer.GetProducer();
		int currentRetry = 1;
		int currentIndex = 0;
		while (currentRetry <= retries) {
			logger.info("Sending text retry " + currentRetry + " of " + retries);
			while (currentIndex < lines.size()) {				
				splitLineAndSendWord(producer, lines.get(currentIndex));									
				currentIndex++;
			}
			currentIndex = 0;
			currentRetry++;
		}		
		producer.close();
	}

	private void splitLineAndSendWord(Producer<Integer, byte[]> producer, String line) {
		for (String word: line.split(" ")) {
			String preparedWord = convert.prepare(word);
			if (!preparedWord.equals("")) {
				sendWord(producer, preparedWord);				
			}
		}
	}
	
	private void sendWord(Producer<Integer, byte[]> producer, String word) {
		Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
		hashMap.put("word", word.getBytes());
		try {
			KeyedMessage<Integer, byte[]> keyedMessage = 
			new	KeyedMessage<Integer, byte[]>(this.topicName, convert.toByteFrom(hashMap));			
			producer.send(keyedMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) throws IOException {
		String topicName = "wordCountTopic";
		int retries = 35;
		if (args.length > 0) {
			topicName = args[0];
			retries = Integer.parseInt(args[1]);
		}	
		
		logger.info("Starting producer");	
		AppWordCountProducer kafkaProducer = new AppWordCountProducer(topicName, retries);
		kafkaProducer.start();
		logger.info("Producer stopped");
	}
}
