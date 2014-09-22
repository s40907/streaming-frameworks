package lan.s40907.protopubKafka.helper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class ProducerPerformance {
	private static Logger logger = Logger.getLogger(ProducerPerformance.class);
	 public static void main(String[] args) throws Exception {
		 
		 /*if (args.length != 3) {
		 System.err.println("USAGE: java " + ProducerPerformance.class.getName() + " url num_records record_size");
		 System.exit(1);
		 }*/
		 //String url = args[0];
		 int numRecords = 10000000;
		 int recordSize;
		 
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("word","KafkaIntegerGenerator");
			hashMap.put("count", "100");
			byte[] payload = toByteFrom(hashMap);
			recordSize = payload.length;
		 
		 Properties props = new Properties();
		 props.put("request.required.acks", "1");
		 props.put("metadata.broker.list", "localhost:9092");
		 props.put("producer.type", "async");
		 props.put("batch.size", recordSize * 2);
		 //properties.put("serializer.class", "kafka.serializer.StringEncoder");
		 //props.put("","");
		 /*
		 props.setProperty(ProducerConfig.REQUIRED_ACKS_CONFIG, "1");
		 props.setProperty(ProducerConfig.BROKER_LIST_CONFIG, url);
		 props.setProperty(ProducerConfig.METADATA_FETCH_TIMEOUT_CONFIG, Integer.toString(5 * 1000));
		 props.setProperty(ProducerConfig.REQUEST_TIMEOUT_CONFIG, Integer.toString(Integer.MAX_VALUE));
		 */
		 Producer<Integer, byte[]> producer = new Producer<Integer, byte[]>(new ProducerConfig(props));
		 /*
		 Callback callback = new Callback() {
		 public void onCompletion(RecordMetadata metadata, Exception e) {
		 if (e != null)
		 e.printStackTrace();
		 }
		 };
		 */
		 

		 //byte[] payload = new byte[recordSize];
		 Arrays.fill(payload, (byte) 1);		 
		 KeyedMessage<Integer,byte[]> keyedMessage = new KeyedMessage<Integer, byte[]>("testOne", payload);
		 long start = System.currentTimeMillis();
		 long maxLatency = -1L;
		 long totalLatency = 0;
		 int reportingInterval = 1000000;
		 for (int i = 0; i < numRecords; i++) {
			 long sendStart = System.currentTimeMillis();		 	 
			 producer.send(keyedMessage);		 
			 long sendEllapsed = System.currentTimeMillis() - sendStart;
			 maxLatency = Math.max(maxLatency, sendEllapsed);
			 totalLatency += sendEllapsed;
			 if (i % reportingInterval == 0) {
				 logger.info( String.format("%d max latency = %d ms, avg latency = %.5f\n",
						 i,
						 maxLatency,
						 (totalLatency / (double) reportingInterval)));
				 totalLatency = 0L;
				 maxLatency = -1L;
			 }
		 }
		 long ellapsed = System.currentTimeMillis() - start;
		 double msgsSec = 1000.0 * numRecords / (double) ellapsed;
		 double mbSec = msgsSec * (recordSize + 12) / (1024.0 * 1024.0);
		 logger.info(String.format("%d records sent in %d ms. %.2f records per second (%.2f mb/sec).", numRecords, ellapsed, msgsSec, mbSec));
		 logger.info("record size: " + recordSize);
		 producer.close();
		 }
	 
		private static <T> byte[] toByteFrom(Map<T, T> hashMap) throws IOException {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();		
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(hashMap);
			return byteArrayOutputStream.toByteArray();
		}
}
