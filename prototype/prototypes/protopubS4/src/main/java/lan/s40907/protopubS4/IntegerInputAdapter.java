package lan.s40907.protopubS4;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;

public class IntegerInputAdapter extends AdapterApp {
	
	private static long amountOfNumbers = 100000000;
	private Convert convert = new Convert();
	private byte[] staticPayload;
	
	public IntegerInputAdapter() {
		staticPayload = new byte[100];
		Arrays.fill(staticPayload, (byte)0);
	}
	
	@Override
	protected void onStart() {
		new Thread(new Runnable() {
			private Logger logger = Logger.getLogger("IntegerInputAdapterRunnable");

			@Override
			public void run() {
				Event event = new Event();				
				event.put("word", String.class, Arrays.toString(staticPayload));				
				String value = event.get("word");
				this.logger.info("Sending following word: " + value);
				
				this.logger.info("start sending integer");
				for (long i = 0; i < amountOfNumbers; i++) {					
					getRemoteStream().put(event);
				}				
				try {
					this.logger.debug("integer of payload: " + convert.toByteFrom(event.getAttributesAsMap()).length);
				} catch (IOException e) {
					this.logger.debug("Cannot convert integer payload to byte from map");
					e.printStackTrace();
				}
				this.logger.info("end sending integer");
			}
			
		}).start();
	}
}
