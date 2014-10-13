package lan.s40907.protopubFlume;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.Logger;

public class WordCountSource extends AbstractSource implements Configurable, PollableSource  {
	private static Logger logger = Logger.getLogger(WordCountSource.class);
	private Convert convert = new Convert();
	private Transpose transpose = new Transpose();
	private String fileToRead = "shaks12.txt";
	private List<String> lines;
	
	@Override
	public void configure(Context context) {
		logger.info("configuration");
		logger.info("Reading text");
		lines = transpose.readAllLines("/" + fileToRead);
		logger.info("found " + lines.size() + " line(s)");		
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = Status.READY;		
		logger.info("start sending WordCount.");
		int currentIndex = 0;
		while (currentIndex < lines.size()) {				
			splitLineAndSendWord(lines.get(currentIndex));									
			currentIndex++;
		}			
		logger.info("end sending WordCount.");		
		return status;
	}

	private void splitLineAndSendWord(String line) {
		for (String word: line.split(" ")) {
			String preparedWord = convert.prepare(word);
			if (!preparedWord.equals("")) {
				sendWord(preparedWord);				
			}
		}
	}
	
	private void sendWord(String word) {
		Map<String, byte[]> hashMap = new HashMap<String, byte[]>();
		hashMap.put("word", word.getBytes());
		try {
			Event event = EventBuilder.withBody(convert.toByteFrom(hashMap));
			getChannelProcessor().processEvent(event);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
}
