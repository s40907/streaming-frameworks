package lan.s40907.websocketclient;

import java.io.IOException;

public interface IWebSocketClient {
	void connect();
	void send(String message) throws IOException, InterruptedException;
}
