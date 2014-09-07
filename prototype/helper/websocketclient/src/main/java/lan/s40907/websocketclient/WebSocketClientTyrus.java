package lan.s40907.websocketclient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.MessageHandler;

import org.glassfish.tyrus.client.ClientManager;


public class WebSocketClientTyrus implements IWebSocketClient {	
	private List<WebSocketMessageListener> webSocketMessageListeners = new ArrayList<WebSocketMessageListener>();	
	private URI webSocketUri;
	private List<String> subProtocols;
	private Session session;
		
	public void setup(IWebSocketConfiguration webSocketConfiguration) throws URISyntaxException {
		webSocketUri = getWebSocketUri(webSocketConfiguration.getIpAddress(), webSocketConfiguration.getPort());
		subProtocols = webSocketConfiguration.getSubProtocols();	
	}
	
	public void addMessageListener(WebSocketMessageListener webSocketMessageListener) {
		webSocketMessageListeners.add(webSocketMessageListener);
	}	
	
	public void connect() {
		try {
			final ClientEndpointConfig configuration = ClientEndpointConfig.Builder.create()
					.preferredSubprotocols(subProtocols)
					.build();
			ClientManager client = ClientManager.createClient();
			client.connectToServer(
					new Endpoint() {

						@Override
						public void onOpen(Session session,EndpointConfig config) {							
							WebSocketClientTyrus.this.session = session;							
							WebSocketClientTyrus.this.session.addMessageHandler(new MessageHandler.Whole<String>() {

				                @Override
				                public void onMessage(String message) {
				                    publishMessage(message);
				                }
				            });							
						}

					},
					configuration, webSocketUri);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String message) throws IOException, InterruptedException{
		session.getBasicRemote().sendText(message);
	}
	
	public void close() throws IOException {
		webSocketMessageListeners = null;		
		session.close();
	}
	
	private void publishMessage(String message) {
		for(WebSocketMessageListener webSocketMessageListener : webSocketMessageListeners) {
			webSocketMessageListener.messageRecieved(message);
		}
	}
	
	private URI getWebSocketUri(String ipAddress, String port) throws URISyntaxException{
		return new URI(String.format("ws://%s:%s", ipAddress, port));
	}
}
