package lan.s40907.protopubS4;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;
import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.core.ProcessingElement;
import com.sun.management.OperatingSystemMXBean;

public class WordCountProcessingElement extends ProcessingElement {
	private static final Logger logger = Logger.getLogger(WordCountProcessingElement.class);
	private HashMap<String, Integer> counter = new HashMap<String, Integer>();
	private long currentTimeSeconds = System.currentTimeMillis();
	private Transpose transpose = new Transpose();
	private int normalizedSecond = 0;
	private int countPerSecond = 0;
	
	@Override
	protected void onCreate() {}

	@Override
	protected void onRemove() {}
	
	public void onEvent(Event event) {
		String value = event.get("word");
		if (counter.containsKey(value)) {
			Integer amount = counter.get(value);
			counter.put(value, ++amount);
		} else {
			counter.put(value, 1);
		}		
		printAndSentMessagesPerSecondToWebSocket();
		countPerSecond++;
	}
	
	private void printAndSentMessagesPerSecondToWebSocket() {
		if ( (currentTimeSeconds + 1000) <  System.currentTimeMillis() ) {
			String takeFirstHeighest = transpose.takeFirstHighest(5, counter);			
			OperatingSystemMXBean operatingSystem = (com.sun.management.OperatingSystemMXBean)
					ManagementFactory.getOperatingSystemMXBean();
			logger.info(String.format("%s, %s, %s", normalizedSecond, countPerSecond, operatingSystem.getSystemCpuLoad()));
			sendToWebSocket(takeFirstHeighest);
			
			normalizedSecond = normalizedSecond + 1;
			countPerSecond = 1;
			currentTimeSeconds = System.currentTimeMillis();
		}
	};
	
	private void sendToWebSocket(String wordCount) {
		String json = String.format("{\"remoteType\": \"backend\", "
				+ "\"countPerSecond\": \"%s\", %s}", countPerSecond, wordCount);		
		try {
			IWebSocketClient client = WebSocketClientFactory.getInstance();
	   		client.send(json);
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
	}
}
