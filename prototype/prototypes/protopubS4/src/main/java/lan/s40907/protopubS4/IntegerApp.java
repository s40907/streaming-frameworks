package lan.s40907.protopubS4;

import org.apache.log4j.Logger;
import org.apache.s4.core.App;

public class IntegerApp extends App {
	private static final Logger logger = Logger.getLogger(IntegerApp.class);

	@Override
	protected void onClose() {}

	@Override
	protected void onInit() {
		logger.info("initialization");
		IntegerProcessingElement integerProcessingElement = createPE(IntegerProcessingElement.class);
		integerProcessingElement.setSingleton(true);
		createInputStream("words", integerProcessingElement);		
	}

	@Override
	protected void onStart() {}
}
