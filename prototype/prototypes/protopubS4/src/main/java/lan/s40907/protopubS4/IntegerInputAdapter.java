package lan.s40907.protopubS4;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;

public class IntegerInputAdapter extends AdapterApp {
	private static Logger logger = Logger.getLogger(IntegerInputAdapter.class);
	private static long amountOfNumbers = 100000000;
	private Convert convert = new Convert();
	
	@Override
	protected void onStart() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Event event = new Event();
				event.put("word", String.class, "s40907");
				logger.info("start sending integer");
				for (long i = 0; i < amountOfNumbers; i++) {					
					getRemoteStream().put(event);
				}				
				try {
					logger.debug("integer of payload: " + convert.toByteFrom(event.getAttributesAsMap()).length);
				} catch (IOException e) {
					logger.debug("Cannot convert integer payload to byte from map");
					e.printStackTrace();
				}
				logger.info("end sending integer");
			}
			
		}).start();
	}
}
