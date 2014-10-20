package lan.s40907.websocketclient;

import java.util.Iterator;
import java.util.ServiceLoader;


public class WebSocketClientFactory {	
	private static IWebSocketClient webSocketClient;
	
	public static IWebSocketClient getInstance() throws Exception {		
		if (webSocketClient == null) {
			IWebSocketConfiguration webSocketConfiguration = getService(IWebSocketConfiguration.class);
			webSocketClient = getService(IWebSocketClient.class);
			webSocketClient.setup(webSocketConfiguration);			
			webSocketClient.connect();
		}
		return webSocketClient;
	}
	
	private static <T> T getService(Class<T> classToLoad) throws InstantiationException, IllegalAccessException, ClassNotFoundException {	
		ServiceLoader<T> serviceLoader = (ServiceLoader<T>) ServiceLoader.load(classToLoad);
		try {
			return getSingle(serviceLoader.iterator());
		} catch (IllegalAccessException e) {
			return null;			
		}
	}
	
	private static <T> T getSingle(Iterator<T> iterator) throws IllegalAccessException, ClassNotFoundException {
		T first = null;
		try {
			first = iterator.next();	
		} catch (Exception e) {			
			throw new ClassNotFoundException("Check resources META-INF", e);
		}
		
		
		
		if (first != null && !iterator.hasNext()) {
			return first;
		} else {			
			throw new IllegalAccessException("Collection has no or more element(s). A single expects one element.");
		}
	}
}
