package lan.s40907.protopubS4;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.base.KeyFinder;
import org.apache.s4.core.App;

public class IntegerApp extends App {
	private static final Logger logger = Logger.getLogger(IntegerApp.class);

	@Override
	protected void onClose() {}

	@Override
	protected void onInit() {
		logger.info("initialization");
		IntegerProcessingElement integerProcessingElement = createPE(IntegerProcessingElement.class);
		createInputStream("words", new KeyFinder<Event>() {

			@Override
			public List<String> get(Event event) {
				return Arrays.asList(new String[] { event.get("word") });
			}
		}, integerProcessingElement);
		
	}

	@Override
	protected void onStart() {}

}
