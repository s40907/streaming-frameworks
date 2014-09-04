package lan.s40907.websocketclient;


public class App {
    private static final String SENT_MESSAGE = "{\"remoteType\": \"backend\", \"wordCount\": {\"word\": \"HelloProto\", \"count\": \"100\"} }";

    public static void main( String[] args ) {
    	 try {    		 
    		 WebSocketClientTyrus clientTyrus = WebSocketClientFactory.getInstance();
    		 clientTyrus.send(SENT_MESSAGE);
    		 clientTyrus.close();

         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
