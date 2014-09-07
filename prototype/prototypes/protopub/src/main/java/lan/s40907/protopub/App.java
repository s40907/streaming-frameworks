package lan.s40907.protopub;

import lan.s40907.websocketclient.IWebSocketClient;
import lan.s40907.websocketclient.WebSocketClientFactory;

import org.json.simple.JSONObject;

public class App 
{
    public static void main( String[] args ) {
    	 try {
    		 IWebSocketClient clientTyrus = WebSocketClientFactory.getInstance();    		 
    		 
    		 long startTime = System.currentTimeMillis();
    		 while(false||(System.currentTimeMillis()-startTime)<300000)
    		 {
    			 JSONObject json = new JSONObject();
        		 json.put("remoteType", "backend");
        		 json.put("sendTime", System.currentTimeMillis());
        		 
        		 JSONObject wordCount = new JSONObject();
        		 wordCount.put("word", "protopub");
        		 wordCount.put("count", 1);
        		 json.put("wordCount", wordCount);
    			 clientTyrus.send(json.toJSONString());
    		 }    		 
    		 
    		 clientTyrus.close();
    		 
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
