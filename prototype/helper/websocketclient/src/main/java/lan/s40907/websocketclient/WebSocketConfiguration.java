package lan.s40907.websocketclient;

import java.util.Arrays;
import java.util.List;

public class WebSocketConfiguration implements IWebSocketConfiguration {	
	private String IpAddress = "192.168.1.60";
	private String Port = "8080";
	private List<String> SubProtocols = Arrays.asList("echo-protocol");
	
	@Override
	public String getIpAddress() {
		return IpAddress;
	}
	
	@Override
	public String getPort() {
		return Port;
	}
	
	@Override
	public List<String> getSubProtocols() {
		return SubProtocols;
	}
}
