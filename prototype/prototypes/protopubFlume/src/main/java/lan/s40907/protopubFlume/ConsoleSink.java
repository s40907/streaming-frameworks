package lan.s40907.protopubFlume;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.apache.log4j.Logger;
import com.sun.management.OperatingSystemMXBean;

public class ConsoleSink extends AbstractSink implements Configurable {
	private static final Logger logger = Logger.getLogger(ConsoleSink.class);
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();
	private long currentTimeSeconds = System.currentTimeMillis();
	private Convert convert = new Convert();
	private Transpose transpose = new Transpose();
	private int normalizedSecond = 0;
	private int countPerSecond = 0;

	@Override
	public Status process() throws EventDeliveryException {		
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();		
		try {
			transaction.begin();
			Event event = channel.take();
			if(event == null) {
				transaction.rollback();
				return Status.BACKOFF;
			}
			Map<String, byte[]> hashMap = convert.toMapFrom(event.getBody());
			String value = new String(hashMap.get("word"));
			if (counter.containsKey(value)) {
				Integer amount = counter.get(value);
				counter.put(value, ++amount);
			} else {
				counter.put(value, 1);
			}			
			transaction.commit();
			printMessagesPerSecond();
			countPerSecond++;
			return Status.READY;
		} catch (Exception ex) {
			logger.error("no message received: ", ex);
			transaction.rollback();
		} finally {
			transaction.close();
		}
		
		return null;
	}
	
	private void printMessagesPerSecond() {
		if ( (currentTimeSeconds + 1000) <  System.currentTimeMillis() ) {
			String takeFirstHeighest = transpose.takeFirstHighest(1, counter);			
			OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean)
					ManagementFactory.getOperatingSystemMXBean();
			logger.info(String.format("%s, %s, %s, %s", normalizedSecond, countPerSecond, operatingSystem.getSystemCpuLoad(), takeFirstHeighest ));
			normalizedSecond = normalizedSecond + 1;
			countPerSecond = 1;
			currentTimeSeconds = System.currentTimeMillis();
		}
	};

	@Override
	public void configure(Context context) {
		logger.info("configuration");
	}

}
