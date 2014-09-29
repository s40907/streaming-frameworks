package lan.s40907.protopubS4;

import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;

public class IntegerInputAdapter extends AdapterApp {
	private static Logger logger = Logger.getLogger(IntegerInputAdapter.class);
	private static long amountOfNumbers = 10000000;
	
	@Override
	protected void onStart() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Event event = new Event();
				event.put("word", String.class, "S4IntegerGenerator");
				logger.info("start sending integer");
				for (long i = 0; i < amountOfNumbers; i++) {
					getRemoteStream().put(event);
				}
				logger.info("end sending integer");
			}
			
		}).start();
	}
}
