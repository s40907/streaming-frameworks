package lan.s40907.websocketclient;


public class App {
    private static final String SENT_MESSAGE = "{\"remoteType\": \"backend\", \"countPerSecond\": \"1\", \"wordCount\": [{\"word\": \"HelloProto\", \"count\": \"11\"}] }";

    public static void main( String[] args ) {
    	 try {    		 
    		 IWebSocketClient client = WebSocketClientFactory.getInstance();
    		 client.send(SENT_MESSAGE);
    		 client.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
