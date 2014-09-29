package lan.s40907.protopubS4;

import java.util.concurrent.atomic.AtomicLong;
import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MainApp {
    public static void main( String[] args ) {   	 
		IWebSocketClient clientTyrus;
		try {
			clientTyrus = WebSocketClientFactory.getInstance();
			 long startTime = System.currentTimeMillis();
			 AtomicLong counter = new AtomicLong();
			 while( false || (System.currentTimeMillis() - startTime) < 300000) {
				 JSONObject json = new JSONObject();
	    		 json.put("remoteType", "backend");
	    		 json.put("countPerSecond", counter.incrementAndGet());
	    		 
	    		 JSONObject wordCount = new JSONObject();
	    		 wordCount.put("word", "protopub");
	    		 wordCount.put("count", 1);
	    		 
	    		 JSONArray jsonArray = new JSONArray();
	    		 jsonArray.add(wordCount);
	    		 
	    		 json.put("wordCount", jsonArray);
				 clientTyrus.send(json.toJSONString());
			 }
			 clientTyrus.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
