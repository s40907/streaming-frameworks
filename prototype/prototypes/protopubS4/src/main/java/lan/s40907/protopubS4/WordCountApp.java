package lan.s40907.protopubS4;

import org.apache.log4j.Logger;
import org.apache.s4.core.App;

public class WordCountApp extends App {
	private static final Logger logger = Logger.getLogger(WordCountApp.class);

	@Override
	protected void onClose() {}

	@Override
	protected void onInit() {
		logger.info("initialization");		
		WordCountProcessingElement wordCountProcessingElement = createPE(WordCountProcessingElement.class);
		wordCountProcessingElement.setSingleton(true);
		createInputStream("words", wordCountProcessingElement);		
	}
	
	@Override
	protected void onStart() {}

}
