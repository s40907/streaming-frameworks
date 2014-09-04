package lan.s40907.websocketclient;

import java.util.Iterator;
import java.util.ServiceLoader;


public class WebSocketClientFactory {	
	private static WebSocketClientTyrus connector;
	
	public static WebSocketClientTyrus getInstance() throws Exception {		
		if (connector == null) {			
			ServiceLoader<IWebSocketConfiguration> serviceLoader = ServiceLoader.load(IWebSocketConfiguration.class);
			IWebSocketConfiguration webSocketConfiguration = getSingle(serviceLoader.iterator());			
			connector = new WebSocketClientTyrus(webSocketConfiguration);
			connector.connect();			
		}
		return connector;
	}
	
	private static <T> T getSingle(Iterator<T> iterator) throws IllegalAccessException {
		T first = iterator.next();
		if (first != null && !iterator.hasNext()) {
			return first;
		} else {			
			throw new IllegalAccessException("Collection has no or more element(s). A single expects one element.");
		}
	}
}
