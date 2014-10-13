package lan.s40907.protopubFlume;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.Logger;

public class ConsoleSource extends AbstractSource implements Configurable, PollableSource  {
	private static Logger logger = Logger.getLogger(ConsoleSource.class);
	private Convert convert = new Convert();
	private long amountOfNumbers = 10000000;
	
	@Override
	public void configure(Context context) {
		logger.info("configuration");
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;		
		try {			
			Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
			byte[] staticPayload = new byte[100];
			Arrays.fill(staticPayload, (byte)0);
			hashMap.put("word", staticPayload);
			Event event = EventBuilder.withBody(convert.toByteFrom(hashMap));
			logger.info("start sending integer");
			for (long i = 0; i < amountOfNumbers; i++) {				
				getChannelProcessor().processEvent(event);
			}
			logger.info("end sending integer");
		} catch (IOException e) {
			logger.error("not sending message", e);
			status = Status.BACKOFF;
		}		
		return status;
	}
}
