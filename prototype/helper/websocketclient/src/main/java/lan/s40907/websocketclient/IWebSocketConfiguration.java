package lan.s40907.websocketclient;

import java.util.List;

public interface IWebSocketConfiguration {
	String getIpAddress();
	String getPort();
	List<String> getSubProtocols();
}
