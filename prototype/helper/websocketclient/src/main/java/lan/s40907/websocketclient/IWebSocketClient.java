package lan.s40907.websocketclient;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IWebSocketClient {
	void setup(IWebSocketConfiguration webSocketConfiguration) throws URISyntaxException;
	void addMessageListener(WebSocketMessageListener webSocketMessageListener);
	void connect();
	void send(String message) throws IOException, InterruptedException;	
	void close() throws IOException;
}
