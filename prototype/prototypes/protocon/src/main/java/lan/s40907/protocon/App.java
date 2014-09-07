package lan.s40907.protocon;

import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;
import lan.s40907.websocketclient.WebSocketMessageListener;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class App 
{
	private static Logger logger = Logger.getLogger(App.class.getName());
    public static void main( String[] args ) {
    	 try {
    		 
    		 JSONObject json = new JSONObject();
    		 json.put("remoteType", "ui");
    		 json.put("message", "hello");
    		 
    		 IWebSocketClient client = WebSocketClientFactory.getInstance();
    		 logger.debug("Connection opened");    		 
    		 client.send(json.toJSONString());    		 
    		 client.addMessageListener(new WebSocketMessageListener() {
				
				@Override
				public void messageRecieved(String message) {
					if (!isUiMessage(message)) {
						Measure measure = parse(message);
						logger.log(MeasureLog4jLevel.MEASURE, measure.toString());						
					}
				}
    		 });
    		 
    		 boolean exit = false;
    		 while (!exit) {    			 
    			 try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					logger.error("Thread interupted");					
				}
    		 }
    		 			 
    		 client.close();
    		 logger.debug("Connection closed");    		 

         } catch (Exception e) {
             e.printStackTrace();
         }
    }
    
    private static boolean isUiMessage(String message) {
    	return message.contains("\"remoteType\":\"ui\"");
    }
    
    private static Measure parse(String message) {
    	Measure measure = null;
    	JSONParser jsonParser = new JSONParser();   	
    	try {
    		long recieveTime = System.currentTimeMillis();
    		
    		logger.trace("Trying to parse message: " + message);
    		JSONObject json = (JSONObject)jsonParser.parse(message);    		  		
    		
    		logger.trace("parsing sendTime");
			long sendTime = Long.valueOf(json.get("sendTime").toString());
			
			logger.trace("parsing wordCount");
			JSONObject jsonWordCount = (JSONObject) json.get("wordCount");
			
			logger.trace("parsing word");
			String word = jsonWordCount.get("word").toString();
			
			logger.trace("parsing count");
			long count = Long.valueOf(jsonWordCount.get("count").toString());			
			
			measure = new Measure();
			measure.recieveTime = recieveTime;
			measure.sendTime = sendTime;
			measure.word = word;
			measure.count = count;
		} catch (ParseException e) {
			logger.error("Cannot parse json", e);
		}    	
    	return measure;
    }
}
