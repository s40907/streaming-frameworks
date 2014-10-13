package lan.s40907.protopubS4;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;

public class WordCountInputAdapter extends AdapterApp {
	private static Logger logger = Logger.getLogger(WordCountInputAdapter.class);	
	private static int maxRetries = 40;
	private Transpose transpose;
	private Convert convert;
	private String fileToRead = "shaks12.txt";
	private static List<String> lines;
	
	public WordCountInputAdapter() {
		transpose = new Transpose();
		convert = new Convert();
	}
	
	@Override
	protected void onInit() {
		super.onInit();
		logger.info("initialization");
		logger.info("Reading text");
		lines = transpose.readAllLines("/" + fileToRead);
		logger.info("found " + lines.size() + " line(s)");
	};
	
	@Override
	protected void onStart() {
		int currentRetry = 0;
		logger.debug("starting");
		logger.debug("line amount while starting " + lines.size());
		while (currentRetry < maxRetries) {
			currentRetry++;
			logger.debug("start sending WordCount retry " + currentRetry);
			int currentIndex = 0;
			while (currentIndex < lines.size()) {
				logger.trace("currentIndex: " + currentIndex);
				String currentValue = lines.get(currentIndex);
				logger.trace("currentIndex value: " + currentValue);						
				splitLineAndSendWord(currentValue);									
				currentIndex++;
			}			
			logger.debug("end sending WordCount retry " + currentRetry);
		}
	}	
	
	private void splitLineAndSendWord(String line) {
		for (String word: line.split(" ")) {
			String preparedWord = convert.prepare(word);
			logger.trace("Prepared word: " + preparedWord);
			if (!preparedWord.equals("")) {
				sendWord(preparedWord);				
			}
		}
	}
	
	private void sendWord(String word) {				
		Event event = new Event();
		event.put("word", String.class, word);
		if (getRemoteStream() != null) {
			getRemoteStream().put(event);
		} else
		{
			logger.error("Cannot get remotestream.");
		}		
	}
	
}
