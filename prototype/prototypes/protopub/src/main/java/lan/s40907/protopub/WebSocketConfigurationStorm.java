package lan.s40907.protopub;

import java.util.Arrays;
import java.util.List;

import lan.s40907.websocketclient.IWebSocketConfiguration;

public class WebSocketConfigurationStorm implements IWebSocketConfiguration {
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